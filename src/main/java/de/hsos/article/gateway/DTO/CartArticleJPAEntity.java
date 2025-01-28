package de.hsos.article.gateway.DTO;

import de.hsos.article.entity.Article;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Base64;

@Entity(name = "cart_article")
public class CartArticleJPAEntity extends ArticleJPAEntity{
    @ManyToOne(cascade = CascadeType.ALL)
    private ArticleJPAEntity article;
    private int quantity;

    public CartArticleJPAEntity() {}

    public CartArticleJPAEntity(ArticleJPAEntity article, int quantity) {
        this.article = article;
        this.quantity = quantity;
    }

    public Article getArticle() {
        return new Article(article.getHeading(), article.getPrice(),
                Base64.getEncoder().encodeToString(article.getImage()), article.id);
    }

    public void setArticle(ArticleJPAEntity article) {
        this.article = article;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
