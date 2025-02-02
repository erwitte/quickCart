package de.hsos.shared;

import de.hsos.article.entity.Article;

public class ArticleConverter {
    public static ArticleDTO articleToArticleDTO(Article article) {
        return new ArticleDTO(article.heading(), article.price(), article.image(), article.id());
    }
}
