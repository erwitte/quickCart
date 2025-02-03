package de.hsos.article.gateway;

import de.hsos.article.entity.Rating;
import de.hsos.article.gateway.DTO.RatingJPAEntity;

public class Converter {
    public static RatingJPAEntity ratingToRatingJPAEntity(Rating rating) {
        RatingJPAEntity ratingJPAEntity = new RatingJPAEntity();
        ratingJPAEntity.setUsername(rating.username());
        ratingJPAEntity.setRating(rating.rating());
        ratingJPAEntity.setReview(rating.review());
        return ratingJPAEntity;
    }
}
