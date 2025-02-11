package de.hsos.article.boundary;

import de.hsos.article.boundary.DTO.ArticleDTO;
import de.hsos.article.boundary.DTO.ArticleDetailsDTO;
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
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/articles")
@Authenticated
@Tag(name = "Articles", description = "Operations related to articles in the webshop.")
public class ArticleResource {
    @Inject
    ArticleService articleService;
    @Inject
    Template fileUpload;
    @Inject
    Template articleDetails;
    @Inject
    JsonWebToken jwt;
    @Inject
    ArticleResourceFallback fallback;

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Upload an article image", description = "Uploads an image for a specific article ID.")
    @APIResponse(responseCode = "201", description = "Image successfully uploaded")
    @APIResponse(responseCode = "401", description = "Unauthorized access")
    @Retry(delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "fallbackSavePicture")
    public Response savePicture(@Parameter(description = "Article ID", required = true)
            @PathParam("id") long id, byte[] image) {
        articleService.safeImage(id, image);
        return Response.status(Response.Status.CREATED).build();
    }

    public Response fallbackSavePicture(long id, byte[] image) {
        return fallback.fallbackSavePicture(id, image);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Create a new article", description = "Creates a new article and returns its ID.")
    @APIResponse(responseCode = "201", description = "Article successfully created")
    @APIResponse(responseCode = "401", description = "Unauthorized access")
    @Retry(delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "fallbackSaveArticle")
    public Response saveArticle(@RequestBody(description = "Article details", required = true, content = @Content(schema = @Schema(implementation = ArticleDTO.class)))
                                    ArticleDTO articleDTO) {
        ArticleWithoutImage articleWithoutImage = new ArticleWithoutImage(articleDTO.getHeading(), articleDTO.getPrice());
        long id = articleService.createArticle(articleWithoutImage);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    public Response fallbackSaveArticle(ArticleDTO articleDTO) {
        return fallback.fallbackSaveArticle(articleDTO);
    }

    @GET
    @RolesAllowed("admin")
    @Operation(summary = "Get all articles", description = "Returns a list of all available articles.")
    @APIResponse(responseCode = "200", description = "List of articles returned")
    @Retry(delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    public Response getFileUpload() {
        TemplateInstance fileUploadInstance = fileUpload.instance();
        return Response.ok().entity(fileUploadInstance).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    @Operation(summary = "Get article by ID", description = "Retrieves a specific article by its ID.")
    @APIResponse(responseCode = "200", description = "Article found", content = @Content(schema = @Schema(implementation = ArticleDTO.class)))
    @APIResponse(responseCode = "404", description = "Article not found")
    @APIResponse(responseCode = "401", description = "Unauthorized access")
    @Retry(delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "fallbackGetArticle")
    public Response getArticle(@Parameter(description = "Article ID", required = true) @PathParam("id") long id) {
        Article article = articleService.getArticle(id);
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ArticleDTO articleDTO = new ArticleDTO(article.heading(), article.price());
        return Response.ok().entity(articleDTO).build();
    }

    public Response fallbackGetArticle(long id) {
        return fallback.fallbackGetArticle(id);
    }

    @GET
    @Path("/{id}/details")
    @RolesAllowed("user")
    @Operation(summary = "Get article details", description = "Retrieves detailed information about an article.")
    @APIResponse(responseCode = "200", description = "Article details retrieved")
    @APIResponse(responseCode = "404", description = "Article not found")
    @APIResponse(responseCode = "401", description = "Unauthorized access")
    @Retry(delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "fallbackGetArticleDetail")
    public Response getArticleDetail(@Parameter(description = "Article ID", required = true) @PathParam("id") long id) {
        Article article = articleService.getArticle(id);
        ArticleDetailsDTO articleDetailsDTO = Converter.articleToArticleDetailsDTO(article);
        TemplateInstance articleDetailsInstance = articleDetails.data("article", articleDetailsDTO);
        return Response.ok().entity(articleDetailsInstance).build();
    }

    public Response fallbackGetArticleDetail(long id) {
        return fallback.fallbackGetArticleDetail(id);
    }

    @PATCH
    @Path("/{id}/details")
    @RolesAllowed("user")
    @Operation(summary = "Add a rating", description = "Allows a user to submit a rating for an article.")
    @APIResponse(responseCode = "204", description = "Rating added successfully")
    @APIResponse(responseCode = "401", description = "Unauthorized access")
    @Retry(delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "fallbackAddRating")
    public Response addRating(@Parameter(description = "Article ID", required = true) @PathParam("id") long id,
                          @RequestBody(description = "Rating details", required = true, content = @Content(schema = @Schema(implementation = ReceiveRatingDTO.class)))
                          ReceiveRatingDTO receiveRatingDTO) {
        String username = jwt.getClaim("preferred_username");
        Rating rating = new Rating(username, receiveRatingDTO.review(), receiveRatingDTO.rating());
        articleService.addRating(id, rating);
        return Response.ok().build();
    }

    public Response fallbackAddRating(long id, ReceiveRatingDTO receiveRatingDTO) {
        return fallback.fallbackAddRating(id, receiveRatingDTO);
    }


    @DELETE
    @RolesAllowed("admin")
    @Path("/{id}")
    @Operation(summary = "Delete an article", description = "Deletes an article by ID.")
    @APIResponse(responseCode = "204", description = "Article deleted successfully")
    @APIResponse(responseCode = "401", description = "Unauthorized access")
    @APIResponse(responseCode = "404", description = "Article not found")
    @Retry(delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "fallbackDeleteArticle")
    public Response deleteArticle(@Parameter(description = "Article ID", required = true) @PathParam("id") long id) {
        articleService.deleteArticle(id);
        return Response.ok().build();
    }

    public Response fallbackDeleteArticle(long id) {
        return fallback.fallbackDeleteArticle(id);
    }
}
