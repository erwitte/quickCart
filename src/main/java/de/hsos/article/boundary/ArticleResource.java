package de.hsos.article.boundary;

import de.hsos.article.boundary.DTO.ArticleDTO;
import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
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
    @Path("/{id}")
    public Response savePicture(@PathParam("id") long id, byte[] picture) {
        articleService.safePicture(id, picture);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveArticle(ArticleDTO articleDTO) {
        Article article = new Article(articleDTO.getHeading(), articleDTO.getPrice());
        long id = articleService.createArticle(article);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    @GET
    public Response getAllArticles() {
        TemplateInstance fileUploadInstance = fileUpload.instance();
        return Response.ok().entity(fileUploadInstance).build();
    }
}
