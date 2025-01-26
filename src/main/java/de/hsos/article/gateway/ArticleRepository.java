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
    public void createArticle(Article article) {
        ArticleJPAEntity articleEntity = new ArticleJPAEntity(article.heading(), article.price(), article.picture());
        Path filePath = Paths.get("a.png");

        try {
            // Write the byte array to the file
            Files.write(filePath, article.picture());
            System.out.println("File saved successfully!");
        } catch (IOException e) {
            System.err.println("Failed to save the file: " + e.getMessage());
        }
        articleEntity.persist();
        ArticleJPAEntity a = findById(1L);
        System.out.println(a);
    }
}
