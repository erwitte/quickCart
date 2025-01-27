package de.hsos.admin.boundary;

import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@ApplicationScoped
@Path("/admin")
@RolesAllowed("admin")
public class AdminResource {
    @Inject
    ArticleService articleService;
    @Inject
    Template fileUpload;
    @Inject
    Template indexAdmin;
    @Inject
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getIndexAdmin(){
        String username = jwt.getClaim("preferred_username");
        List<Article> articles = articleService.getArticles();
        TemplateInstance indexAdminInstance = indexAdmin.data("username", username)
                                                        .data("articles", articles);
        return Response.ok(indexAdminInstance).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/file-upload")
    public Response getFileUploadPage(){
        return Response.ok(fileUpload.render()).build();
    }
}
