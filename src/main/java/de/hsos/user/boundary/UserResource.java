package de.hsos.user.boundary;

import de.hsos.article.control.ArticleService;
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
        TemplateInstance indexUserInstance = indexUser.data("username", username)
                                                        .data("articles", articleService.getArticlesByHeading(article));
        return Response.ok(indexUserInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    private List<ArticleDTO> adjustArticleListToExchaneRate(String currency) {
        exchangeRate = ExchangeRateService.getExchangeRate(currency);
        return articleService.getArticles().stream()
                .map(article -> new ArticleDTO(
                        article.heading(),
                        BigDecimal.valueOf(article.price() * exchangeRate)
                                .setScale(2, RoundingMode.HALF_UP) // Round to 2 decimal places
                                .doubleValue(),
                        article.image(), article.id()
                )).toList();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response indexUser(){
        String username = jwt.getClaim("preferred_username");
        User user = userService.getUser(username);
        List<ArticleDTO> articleList = adjustArticleListToExchaneRate(user.getCurrency());


        if (username == null) {
            // Handle the case where the claim is missing
            return Response.status(Response.Status.UNAUTHORIZED).entity("Username not found in token").build();
        }
        TemplateInstance indexUserInstance = indexUser.data("username", username)
                                                        .data("articles", articleList)
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
