package de.hsos.article.boundary;

import de.hsos.article.boundary.DTO.ArticleDTO;
import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequestScoped
@Path("/articles")
public class ArticleResource {
    @Inject
    ArticleService articleService;
    @Inject
    Template fileUpload;

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response createArticle(byte[] pic) {
        Article article = new Article("articleDTO.getHeading()", 1, pic);
        articleService.createArticle(article);
        return Response.status(Response.Status.CREATED).entity(article).build();
    }

    @GET
    public Response getAllArticles() {
        TemplateInstance fileUploadInstance = fileUpload.instance();
        return Response.ok().entity(fileUploadInstance).build();
    }
}
