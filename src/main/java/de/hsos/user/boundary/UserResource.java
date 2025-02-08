package de.hsos.user.boundary;

import de.hsos.acl.ArticleServiceAdapter;
import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import de.hsos.shared.ArticleDTO;
import de.hsos.shared.PagingService;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Path("/users")
@RequestScoped
@RolesAllowed("user")
@Tag(name = "Users", description = "Operations related to user management and interactions.")
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
    UserService userService;
    double exchangeRate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/search")
    @Operation(summary = "Search for articles", description = "Returns a list of articles that match the search query.")
    @APIResponse(responseCode = "200", description = "Articles returned successfully")
    @APIResponse(responseCode = "401", description = "Unauthorized, only users can access this")
    public Response searchArticles(@Parameter(description = "Article name to search for", required = true)
            @QueryParam("article") String heading) {
        String username = jwt.getClaim("preferred_username");
        User user = userService.getUser(username);
        List<ArticleDTO> articleList = adjustArticleListToExchaneRate(user.getCurrency(), ArticleServiceAdapter.getArticleByHeading(heading));
        TemplateInstance indexUserInstance = indexUser.data("username", username)
                                                        .data("articles", articleList)
                                                                .data("currency", user.getCurrency());
        return Response.ok(indexUserInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    private List<ArticleDTO> adjustArticleListToExchaneRate(String currency, List<ArticleDTO> articles) {
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

    @GET
    @Path("/price")
    @Operation(summary = "Get adjusted price", description = "Returns the price converted to the user's currency.")
    @APIResponse(responseCode = "200", description = "Converted price returned")
    @APIResponse(responseCode = "401", description = "Unauthorized, only users can access this")
    public Response getCorrectPrice(@Parameter(description = "Original price", required = true)
            @QueryParam("price") double price){
        String username = jwt.getClaim("preferred_username");
        User user = userService.getUser(username);
        double exchangeRate = ExchangeRateService.getExchangeRate(user.getCurrency());
        double adjustedPriceCurrency = price * exchangeRate;
        BigDecimal adjustedPriceRounded = BigDecimal.valueOf(adjustedPriceCurrency).setScale(2, RoundingMode.HALF_UP);
        String adjustedPriceWithCurrency = adjustedPriceRounded + user.getCurrency();
        return Response.ok(adjustedPriceWithCurrency).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Get user index page", description = "Returns the user dashboard with paginated articles.")
    @APIResponse(responseCode = "200", description = "User dashboard returned")
    public Response indexUser(
            @Parameter(description = "Page number for pagination", example = "1") @QueryParam("page") @DefaultValue("1") int page,
            @Parameter(description = "Number of articles per page", example = "9") @QueryParam("pageSize") @DefaultValue("9") int pageSize
    ){
        String username = jwt.getClaim("preferred_username");
        User user = userService.getUser(username);
        List<ArticleDTO> articleList = adjustArticleListToExchaneRate(user.getCurrency(), ArticleServiceAdapter.getArticles());
        List<ArticleDTO> pagedArticleList = Collections.emptyList();
        try {
            pagedArticleList = PagingService.getPagedArticleList(page, pageSize, articleList);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Username not found in token").build();
        }
        TemplateInstance indexUserInstance = indexUser.data("username", username)
                                                        .data("articles", pagedArticleList)
                                                        .data("currency", user.getCurrency());
        return Response.ok(indexUserInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    @POST
    @Path("/logout")
    @Operation(summary = "Logout user", description = "Logs out the user from Keycloak.")
    @APIResponse(responseCode = "200", description = "User logged out successfully")
    public Response logOutUser(@RequestBody(description = "Refresh token for logout", required = true,
            content = @Content(schema = @Schema(implementation = RefreshTokenDTO.class)))
            RefreshTokenDTO refreshTokenDTO){
        keycloakAPI.logout(refreshTokenDTO.refreshToken());
        return Response.ok().build();
    }

    @POST
    @PermitAll
    @Operation(summary = "Create a new user", description = "Registers a new user in Keycloak and the application database.")
    @APIResponse(responseCode = "201", description = "User created successfully")
    @APIResponse(responseCode = "400", description = "Bad request, missing data")
    public Response createUser(@RequestBody(description = "New user details", required = true,
            content = @Content(schema = @Schema(implementation = CreateUserDTO.class)))
            CreateUserDTO newUser) {
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
    @Operation(summary = "Get user settings", description = "Returns the user settings page.")
    @APIResponse(responseCode = "200", description = "Settings page returned")
    public Response settings(){
        TemplateInstance settingsInstance = settingsUser.instance();
        return Response.ok(settingsInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    @PATCH
    @Path("/settings")
    @Operation(summary = "Update user data", description = "Updates the user's address and currency settings.")
    @APIResponse(responseCode = "204", description = "User data updated successfully")
    public void updateUserData(@RequestBody(description = "Updated user details", required = true,
            content = @Content(schema = @Schema(implementation = UpdateUserDTO.class)))
            UpdateUserDTO updateUserDTO){
        String username = jwt.getClaim("preferred_username");
        userService.updateUser(username, updateUserDTO.getStreet(), updateUserDTO.getHouseNumber(),
                updateUserDTO.getZipCode(), updateUserDTO.getCity(), updateUserDTO.getCurrency());
    }

    @GET
    @Path("/details")
    @Produces(MediaType.TEXT_HTML)
    @Operation(
            summary = "Get user details",
            description = "Returns a detailed HTML page with user information."
    )
    @APIResponse(
            responseCode = "200",
            description = "User details page returned",
            content = @Content(mediaType = MediaType.TEXT_HTML)
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized, only users can access this"
    )
    public Response getUserDetails(){
        String username = jwt.getClaim("preferred_username");
        User user = userService.getUser(username);
        UserDetailsDTO userDetailsDTO = Converter.userToUserDetailsDTO(user);
        TemplateInstance userDetailsInstance = userDetails.data("userDetails", userDetailsDTO);
        return Response.ok(userDetailsInstance.render()).type(MediaType.TEXT_HTML).build();
    }
}
