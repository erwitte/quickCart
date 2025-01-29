package de.hsos.article.boundary;

import de.hsos.article.boundary.DTO.NewArticleDTO;
import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import de.hsos.article.entity.ArticleWithoutImage;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/articles")
@Authenticated
public class ArticleResource {
    @Inject
    ArticleService articleService;
    @Inject
    Template fileUpload;

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response savePicture(@PathParam("id") long id, byte[] image) {
        articleService.safeImage(id, image);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response saveArticle(NewArticleDTO newArticleDTO) {
        ArticleWithoutImage articleWithoutImage = new ArticleWithoutImage(newArticleDTO.getHeading(), newArticleDTO.getPrice());
        long id = articleService.createArticle(articleWithoutImage);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    @GET
    @PermitAll
    public Response getAllArticles() {
        TemplateInstance fileUploadInstance = fileUpload.instance();
        return Response.ok().entity(fileUploadInstance).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    public Response getArticle(@PathParam("id") long id) {
        Article article = articleService.getArticle(id);
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        NewArticleDTO newArticleDTO = new NewArticleDTO(article.heading(), article.price());
        return Response.ok().entity(newArticleDTO).build();
    }


    @DELETE
    @RolesAllowed("admin")
    @Path("/{id}")
    public void deleteArticle(@PathParam("id") long id) {
        articleService.deleteArticle(id);
    }
}
