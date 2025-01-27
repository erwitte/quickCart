package de.hsos.article.control;

import de.hsos.article.entity.Article;
import de.hsos.article.entity.ArticleWithoutImage;

import java.util.List;

public interface ArticleService {
    long createArticle(ArticleWithoutImage articleWithoutImage);
    void safeImage(long id, byte[] image);
    List<Article> getArticles();
    Article getArticle(long id);
    void deleteArticle(long id);
}
