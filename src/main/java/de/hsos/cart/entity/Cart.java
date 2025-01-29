package de.hsos.cart.entity;

import de.hsos.article.entity.Article;

import java.util.List;

public record Cart(
        List<Article> article
) {
}
