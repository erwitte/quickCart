package de.hsos.article.entity;

public record Article(
        String heading,
        double price,
        String image,
        long id
) {
}
