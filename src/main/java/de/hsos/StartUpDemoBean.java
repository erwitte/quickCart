package de.hsos;

import de.hsos.article.control.ArticleService;
import de.hsos.article.entity.ArticleWithoutImage;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@ApplicationScoped
public class StartUpDemoBean {
    @Inject
    ArticleService articleService;

    public void onStart(@Observes StartupEvent startupEvent) throws IOException {
        long id = articleService.createArticle(new ArticleWithoutImage("chair", 2));
        File image = new File("chair.jpg");
        byte[] imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);

        id = articleService.createArticle(new ArticleWithoutImage("shirt", 3.5));
        image = new File("shirt.jpg");
        imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);

        id = articleService.createArticle(new ArticleWithoutImage("headset", 20));
        image = new File("headset.jpg");
        imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);

        id = articleService.createArticle(new ArticleWithoutImage("couch", 200));
        image = new File("couch.jpg");
        imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);

        id = articleService.createArticle(new ArticleWithoutImage("pan", 4));
        image = new File("pan.jpg");
        imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);

        id = articleService.createArticle(new ArticleWithoutImage("coffee maker", 50));
        image = new File("coffee_maker.jpg");
        imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);

        id = articleService.createArticle(new ArticleWithoutImage("coffee beans", 20));
        image = new File("coffee.jpg");
        imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);

        id = articleService.createArticle(new ArticleWithoutImage("television", 22));
        image = new File("tv.png");
        imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);

        id = articleService.createArticle(new ArticleWithoutImage("playstation", 75));
        image = new File("playstation.jpg");
        imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);

        id = articleService.createArticle(new ArticleWithoutImage("fork", 1));
        image = new File("fork.jpg");
        imageBytes = Files.readAllBytes(image.toPath());
        articleService.safeImage(id, imageBytes);
    }
}
