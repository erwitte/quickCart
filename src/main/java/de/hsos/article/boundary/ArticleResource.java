package de.hsos.article.boundary;

import de.hsos.article.boundary.DTO.ArticleDTO;
import de.hsos.article.boundary.DTO.ArticleDetailsDTO;
import de.hsos.article.boundary.DTO.RatingDTO;
import de.hsos.article.boundary.DTO.ReceiveRatingDTO;
import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import de.hsos.article.entity.ArticleWithoutImage;
import de.hsos.article.entity.Rating;
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
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
@Path("/articles")
@Authenticated
public class ArticleResource {
    @Inject
    ArticleService articleService;
    @Inject
    Template fileUpload;
    @Inject
    Template articleDetails;
    @Inject
    JsonWebToken jwt;

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
    public Response saveArticle(ArticleDTO articleDTO) {
        ArticleWithoutImage articleWithoutImage = new ArticleWithoutImage(articleDTO.getHeading(), articleDTO.getPrice());
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
        ArticleDTO articleDTO = new ArticleDTO(article.heading(), article.price());
        return Response.ok().entity(articleDTO).build();
    }

    @GET
    @Path("/{id}/details")
    @RolesAllowed("user")
    public Response getArticleDetail(@PathParam("id") long id) {
        Article article = articleService.getArticle(id);
        ArticleDetailsDTO articleDetailsDTO = Converter.articleToArticleDetailsDTO(article);
        TemplateInstance articleDetailsInstance = articleDetails.data("article", articleDetailsDTO);
        return Response.ok().entity(articleDetailsInstance).build();
    }

    @PATCH
    @Path("/{id}/details")
    @RolesAllowed("user")
    public void addRating(@PathParam("id") long id, ReceiveRatingDTO receiveRatingDTO) {
        String username = jwt.getClaim("preferred_username");
        Rating rating = new Rating(username, receiveRatingDTO.review(), receiveRatingDTO.rating());
        articleService.addRating(id, rating);
    }


    @DELETE
    @RolesAllowed("admin")
    @Path("/{id}")
    public void deleteArticle(@PathParam("id") long id) {
        articleService.deleteArticle(id);
    }
}
