package de.hsos.acl;

import de.hsos.article.control.ArticleService;
import de.hsos.article.gateway.ArticleRepository;
import de.hsos.shared.ArticleConverter;
import de.hsos.shared.ArticleDTO;

import java.util.List;

public class ArticleServiceAdapter {
    private static final ArticleService articleService = new ArticleRepository();

    public static List<ArticleDTO> getArticles() {
        return articleService.getArticles().stream().map(ArticleConverter::articleToArticleDTO).toList();
    }

    public static List<ArticleDTO> getArticleByHeading(String heading) {
        return articleService.getArticlesByHeading(heading).stream().map(ArticleConverter::articleToArticleDTO).toList();
    }
}
