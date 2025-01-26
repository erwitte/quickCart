package de.hsos.article.control;

import de.hsos.article.entity.Article;

public interface ArticleService {
    long createArticle(Article article);
    void safePicture(long id, byte[] picture);
}
