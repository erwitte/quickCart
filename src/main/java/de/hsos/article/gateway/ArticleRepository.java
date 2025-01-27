package de.hsos.article.gateway;

import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.Article;
import de.hsos.article.entity.ArticleWithoutImage;
import de.hsos.article.gateway.DTO.ArticleJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
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
        System.out.println("oidsngdsgodsagosakg");
    }

    @Override
    public List<Article> getArticles(){
        List<ArticleJPAEntity> articleJPAEntities = listAll();
        return articleJPAEntities.stream()
                .map(entity -> new Article(
                        entity.getHeading(),
                        entity.getPrice(),
                        entity.getImage()
                ))
                .toList();
    }
}
