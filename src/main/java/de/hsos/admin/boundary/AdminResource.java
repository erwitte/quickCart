package de.hsos.admin.boundary;

import de.hsos.acl.ArticleServiceAdapter;
import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import de.hsos.shared.ArticleConverter;
import de.hsos.shared.ArticleDTO;
import de.hsos.shared.PagingService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@ApplicationScoped
@Path("/admin")
@RolesAllowed("admin")
@Tag(name = "Admin", description = "Admin operations for managing articles and file uploads.")
public class AdminResource {
    @Inject
    Template fileUpload;
    @Inject
    Template indexAdmin;
    @Inject
    JsonWebToken jwt;
    @Inject
    AdminResourceFallback fallback;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Operation(
            summary = "Get admin index page",
            description = "Returns the admin dashboard page with paginated articles."
    )
    @APIResponse(
            responseCode = "200",
            description = "Admin dashboard returned",
            content = @Content(mediaType = MediaType.TEXT_HTML)
    )
    @APIResponse(responseCode = "401", description = "Forbidden, only admins can access this")
    @Retry(maxRetries = 3, delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "fallbackIndexAdmin")
    public Response getIndexAdmin(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("pageSize") @DefaultValue("9") int pageSize
    ){
        String username = jwt.getClaim("preferred_username");
        List<ArticleDTO> articles = ArticleServiceAdapter.getArticles();
        List<ArticleDTO> pagedArticles = PagingService.getPagedArticleList(page, pageSize, articles);
        TemplateInstance indexAdminInstance = indexAdmin.data("username", username)
                                                        .data("articles", pagedArticles);
        return Response.ok(indexAdminInstance).build();
    }

    public Response fallbackIndexAdmin(int page, int pageSize) {
        return fallback.fallbackIndexAdmin(page, pageSize);
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/file-upload")
    @Operation(
            summary = "Get file upload page",
            description = "Returns the file upload page for administrators."
    )
    @APIResponse(
            responseCode = "200",
            description = "File upload page returned",
            content = @Content(mediaType = MediaType.TEXT_HTML)
    )
    @APIResponse(responseCode = "401", description = "Forbidden, only admins can access this")
    @Retry(maxRetries = 2, delay = 300)
    @Timeout(1500)
    @CircuitBreaker(requestVolumeThreshold = 3, failureRatio = 0.4, delay = 4000)
    @Fallback(fallbackMethod = "fallbackFileUploadPage")
    public Response getFileUploadPage(){
        return Response.ok(fileUpload.render()).build();
    }

    public Response fallbackFileUploadPage() {
        return fallback.fallbackFileUploadPage();
    }
}
