package de.hsos.article.gateway;

import de.hsos.article.entity.Rating;
import de.hsos.article.gateway.DTO.RatingJPAEntity;

public class Converter {
    public static RatingJPAEntity ratingToRatingJPAEntity(Rating rating) {
        return new RatingJPAEntity(rating.username(), rating.review(), rating.rating());
    }
}
