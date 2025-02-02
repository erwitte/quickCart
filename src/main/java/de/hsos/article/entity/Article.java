package de.hsos.article.entity;

import java.util.List;

public record Article(
        String heading,
        double price,
        String image,
        long id,
        List<Rating> ratingList
) {
}
