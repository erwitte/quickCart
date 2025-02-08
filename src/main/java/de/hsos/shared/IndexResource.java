package de.hsos.shared;

import de.hsos.acl.ArticleServiceAdapter;
import de.hsos.article.control.ArticleService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@RequestScoped
@Path("/")
@Tag(name = "Public", description = "Endpoints for public-facing pages like index, register, and login.")
public class IndexResource {
    @Inject
    Template index;
    @Inject
    Template register;
    @Inject
    @RestClient
    KeycloakAPI keycloakAPI;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Operation(
            summary = "Get the index page",
            description = "Returns the index page with paginated articles."
    )
    @APIResponse(
            responseCode = "200",
            description = "Index page returned",
            content = @Content(mediaType = MediaType.TEXT_HTML)
    )
    public Response index(
            @Parameter(description = "Page number for pagination", example = "1") @QueryParam("page") @DefaultValue("1") int page,
            @Parameter(description = "Number of articles per page", example = "9")@QueryParam("pageSize") @DefaultValue("9") int pageSize
    ) {
        List<ArticleDTO> articles = ArticleServiceAdapter.getArticles();
        List<ArticleDTO> pagedArticles = PagingService.getPagedArticleList(page, pageSize, articles);
        TemplateInstance indexInstance = index.data("articles", pagedArticles);
        return Response.ok(indexInstance).type(MediaType.TEXT_HTML).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("register")
    @Operation(
            summary = "Get the registration page",
            description = "Returns the registration page for new users."
    )
    @APIResponse(
            responseCode = "200",
            description = "Registration page returned",
            content = @Content(mediaType = MediaType.TEXT_HTML)
    )
    public Response register() {
        TemplateInstance registerInstance = register.instance();
        return Response.ok(registerInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    @POST
    @Path("login")
    @Operation(
            summary = "Login to the system",
            description = "Logs in a user by sending credentials to Keycloak."
    )
    @APIResponse(responseCode = "200", description = "Login successful, returns token")
    @APIResponse(responseCode = "401", description = "Unauthorized, incorrect credentials")
    @APIResponse(responseCode = "500", description = "Internal server error")
    public Response login(@RequestBody(
            description = "User login credentials",
            required = true,
            content = @Content(schema = @Schema(implementation = LoginDTO.class))
    )
            LoginDTO loginDTO) {
        return keycloakAPI.receiveToken(
                "quarkus-be",
                "Ffw0GGjHrfTFnLb8mHFlBGLjcIL8hpfO",
                "password",
                loginDTO.username(),
                loginDTO.password()
        );
    }
}
