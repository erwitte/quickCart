package de.hsos.article.entity;

import java.util.List;

public record Cart(
        List<Article> article
) {
}
