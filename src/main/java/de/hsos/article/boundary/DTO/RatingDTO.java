package de.hsos.article.boundary.DTO;

public record RatingDTO(
        String username,
        String review,
        int rating
) {
}
