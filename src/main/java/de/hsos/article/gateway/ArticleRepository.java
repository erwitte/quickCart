package de.hsos.article.gateway;

import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import de.hsos.article.entity.ArticleWithoutImage;
import de.hsos.article.gateway.DTO.ArticleJPAEntity;
import de.hsos.article.gateway.DTO.CartJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class ArticleRepository implements ArticleService, PanacheRepository<ArticleJPAEntity> {

    @Override
    @Transactional
    public long createArticle(ArticleWithoutImage articleWithoutImage) {
        ArticleJPAEntity articleEntity = new ArticleJPAEntity();
        articleEntity.setHeading(articleWithoutImage.heading());
        articleEntity.setPrice(articleWithoutImage.price());
        articleEntity.persist();
        return articleEntity.id;
    }

    @Override
    @Transactional
    public void safeImage(long id, byte[] image) {
        ArticleJPAEntity articleEntity = findById(id);
        articleEntity.setImage(image);
        articleEntity.persist();
    }

    @Override
    @Transactional
    public List<Article> getArticles(){
        List<ArticleJPAEntity> articleJPAEntities = listAll();
        return articleJPAEntities.stream()
                .map(this::articleJPAEntityToArticle)
                .toList();
    }

    @Override
    @Transactional
    public List<Article> getArticlesByHeading(String query){
        List<ArticleJPAEntity> articleJPAEntities = find("heading", query).list();
        return articleJPAEntities.stream()
                .map(this::articleJPAEntityToArticle)
                .toList();
    }

    private Article articleJPAEntityToArticle(ArticleJPAEntity articleJPAEntity) {
        return new Article(articleJPAEntity.getHeading(), articleJPAEntity.getPrice(),
                Base64.getEncoder().encodeToString(articleJPAEntity.getImage()), articleJPAEntity.id);
    }

    @Override
    public Article getArticle(long id) {
        ArticleJPAEntity articleEntity = findById(id);
        return new Article(articleEntity.getHeading(), articleEntity.getPrice(), Base64.getEncoder().encodeToString(articleEntity.getImage()), id);
    }

    @Override
    @Transactional
    public void deleteArticle(long id) {
        ArticleJPAEntity articleEntity = findById(id);
        articleEntity.delete();
    }

    @Override
    public ArticleJPAEntity getJPAArticle(long id) {
        return findById(id);
    }
}
