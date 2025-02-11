package de.hsos.article.boundary;

import de.hsos.article.boundary.DTO.ArticleDTO;
import de.hsos.article.boundary.DTO.ReceiveRatingDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ArticleResourceFallback {

    public Response fallbackSavePicture(long id, byte[] image) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Unable to upload image at the moment. Please try again later.")
                .build();
    }

    public Response fallbackSaveArticle(ArticleDTO articleDTO) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Unable to create article at the moment. Please try again later.")
                .build();
    }

    public Response fallbackGetAllArticles() {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Unable to retrieve articles at the moment. Please try again later.")
                .build();
    }

    public Response fallbackGetArticle(long id) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Unable to retrieve article at the moment. Please try again later.")
                .build();
    }

    public Response fallbackGetArticleDetail(long id) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Unable to retrieve article details at the moment. Please try again later.")
                .build();
    }

    public Response fallbackAddRating(long id, ReceiveRatingDTO receiveRatingDTO) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Failed to add rating. Please try again later.")
                .build();
    }

    public Response fallbackDeleteArticle(long id) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Failed to delete article. Please try again later.")
                .build();
    }
}
