package de.hsos.article.entity;

public record Rating(
        String username,
        String review,
        int rating
) {
}
