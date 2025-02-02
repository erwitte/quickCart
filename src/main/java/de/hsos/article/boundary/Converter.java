package de.hsos.article.boundary;

import de.hsos.article.boundary.DTO.ArticleDetailsDTO;
import de.hsos.article.boundary.DTO.RatingDTO;
import de.hsos.article.entity.Article;

import java.util.List;

public class Converter {
    public static ArticleDetailsDTO articleToArticleDetailsDTO(Article article) {
        List<RatingDTO> ratingDTOs = article.ratingList().stream()
                .map(rating -> new RatingDTO(rating.username(), rating.review(), rating.rating())).toList();
        return new ArticleDetailsDTO(article.heading(), article.price(), article.image(), article.id(),ratingDTOs);
    }
}
