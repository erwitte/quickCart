package de.hsos.article.entity;

public record Article(
        String heading,
        double price,
        byte[] picture
) {
}
