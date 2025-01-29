package de.hsos.user.boundary.DTO;

public record ArticleDTO(
        String heading,
        double price,
        String image,
        long id
) {
}
