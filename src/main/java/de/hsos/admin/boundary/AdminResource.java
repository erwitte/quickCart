package de.hsos.admin.boundary;

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
    public Response getIndexAdmin(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("pageSize") @DefaultValue("9") int pageSize
    ){
        String username = jwt.getClaim("preferred_username");
        List<ArticleDTO> articles = articleService.getArticles().stream().map(ArticleConverter::articleToArticleDTO).toList();
        List<ArticleDTO> pagedArticles = PagingService.getPagedArticleList(page, pageSize, articles);
        TemplateInstance indexAdminInstance = indexAdmin.data("username", username)
                                                        .data("articles", pagedArticles);
        return Response.ok(indexAdminInstance).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/file-upload")
    public Response getFileUploadPage(){
        return Response.ok(fileUpload.render()).build();
    }
}
