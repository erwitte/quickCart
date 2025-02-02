package de.hsos.user.boundary;

import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import de.hsos.shared.ArticleDTO;
import de.hsos.user.boundary.DTO.*;
import de.hsos.shared.KeycloakAPI;
import de.hsos.shared.KeycloakManager;
import de.hsos.user.control.UserService;
import de.hsos.user.entity.User;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Path("/users")
@RequestScoped
@RolesAllowed("user")
public class UserResource {
    @Inject
    Template indexUser;
    @Inject
    Template settingsUser;
    @Inject
    Template userDetails;
    @Inject
    @RestClient
    KeycloakAPI keycloakAPI;
    @Inject
    KeycloakManager keycloakManager;
    @Inject
    JsonWebToken jwt;
    @Inject
    ArticleService articleService;
    @Inject
    UserService userService;
    double exchangeRate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/search")
    public Response searchArticles(@QueryParam("article") String article) {
        String username = jwt.getClaim("preferred_username");
        User user = userService.getUser(username);
        List<ArticleDTO> articleList = adjustArticleListToExchaneRate(user.getCurrency(), articleService.getArticlesByHeading(article));
        TemplateInstance indexUserInstance = indexUser.data("username", username)
                                                        .data("articles", articleList)
                                                                .data("currency", user.getCurrency());
        return Response.ok(indexUserInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    private List<ArticleDTO> adjustArticleListToExchaneRate(String currency, List<Article> articles) {
        exchangeRate = ExchangeRateService.getExchangeRate(currency);
        return articles.stream()
                .map(article -> new ArticleDTO(
                        article.heading(),
                        BigDecimal.valueOf(article.price() * exchangeRate)
                                .setScale(2, RoundingMode.HALF_UP) // round to two decimal places
                                .doubleValue(),
                        article.image(), article.id()
                )).toList();
    }

    private List<ArticleDTO> getPagedArticleList(int page, int pageSize, List<ArticleDTO> articles) {
        // validation
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero.");
        }
        if (page < 1) {
            throw new IllegalArgumentException("Page number must be at least 1.");
        }
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, articles.size());
        if (fromIndex >= articles.size()) {
            return Collections.emptyList(); // Return empty list if page exceeds available items
        }

        return articles.subList(fromIndex, toIndex);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response indexUser(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("pageSize") @DefaultValue("9") int pageSize
    ){
        String username = jwt.getClaim("preferred_username");
        User user = userService.getUser(username);
        List<ArticleDTO> articleList = adjustArticleListToExchaneRate(user.getCurrency(), articleService.getArticles());
        List<ArticleDTO> pagedArticleList = Collections.emptyList();
        try {
            pagedArticleList = getPagedArticleList(page, pageSize, articleList);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        if (username == null) {
            // Handle the case where the claim is missing
            return Response.status(Response.Status.UNAUTHORIZED).entity("Username not found in token").build();
        }
        TemplateInstance indexUserInstance = indexUser.data("username", username)
                                                        .data("articles", pagedArticleList)
                                                        .data("currency", user.getCurrency());
        return Response.ok(indexUserInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    @POST
    @Path("/logout")
    public Response logOutUser(RefreshTokenDTO refreshTokenDTO){
        keycloakAPI.logout(refreshTokenDTO.refreshToken());
        return Response.ok().build();
    }

    @POST
    @PermitAll
    public Response createUser(CreateUserDTO newUser) {
        String keycloakManagerAccessToken = "Bearer " + keycloakManager.getAccessToken();
        Response wasCreated = keycloakAPI.createUser(new CreateUserKeycloakDTO(newUser.getUsername(),
                                                    newUser.getPassword(),newUser.getEmail()), keycloakManagerAccessToken);
        if (wasCreated.getStatus() == 201){
            userService.createUser(new User(newUser));
            keycloakManager.logOutKeycloakManager();
        }
        return wasCreated;
    }

    @GET
    @Path("/settings")
    public Response settings(){
        TemplateInstance settingsInstance = settingsUser.instance();
        return Response.ok(settingsInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    @PATCH
    @Path("/settings")
    public void updateUserData(UpdateUserDTO updateUserDTO){
        String username = jwt.getClaim("preferred_username");
        userService.updateUser(username, updateUserDTO.getStreet(), updateUserDTO.getHouseNumber(),
                updateUserDTO.getZipCode(), updateUserDTO.getCity(), updateUserDTO.getCurrency());
    }

    @GET
    @Path("/details")
    @Produces(MediaType.TEXT_HTML)
    public Response getUserDetails(){
        String username = jwt.getClaim("preferred_username");
        User user = userService.getUser(username);
        UserDetailsDTO userDetailsDTO = Converter.userToUserDetailsDTO(user);
        TemplateInstance userDetailsInstance = userDetails.data("userDetails", userDetailsDTO);
        return Response.ok(userDetailsInstance.render()).type(MediaType.TEXT_HTML).build();
    }
}
