package de.hsos.article.gateway;

import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import de.hsos.article.gateway.DTO.ArticleJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequestScoped
public class ArticleRepository implements ArticleService, PanacheRepository<ArticleJPAEntity> {

    @Override
    @Transactional
    public long createArticle(Article article) {
        ArticleJPAEntity articleEntity = new ArticleJPAEntity();
        articleEntity.setHeading(article.heading());
        articleEntity.setPrice(article.price());
        articleEntity.persist();
        return articleEntity.id;
    }

    @Override
    @Transactional
    public void safePicture(long id, byte[] picture) {
        ArticleJPAEntity articleEntity = findById(id);
        articleEntity.setPicture(picture);
        articleEntity.persist();
        ArticleJPAEntity a = findById(id);
        System.out.println(a);
    }
}
