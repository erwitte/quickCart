package de.hsos.article.boundary.DTO;

import java.util.List;

public record ArticleDetailsDTO(
        String heading,
        double price,
        String image,
        long id,
        List<RatingDTO> ratings
) {
}
