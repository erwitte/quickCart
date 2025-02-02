package de.hsos.shared;

public record ArticleDTO(
        String heading,
        double price,
        String image,
        long id
) {
}
