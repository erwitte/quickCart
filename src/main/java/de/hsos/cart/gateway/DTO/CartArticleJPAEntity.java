package de.hsos.cart.gateway.DTO;

import de.hsos.article.gateway.DTO.ArticleJPAEntity;
import jakarta.persistence.Entity;

@Entity(name = "cart_article")
public class CartArticleJPAEntity extends ArticleJPAEntity {
    private long articleId;
    private int quantity;

    public CartArticleJPAEntity() {}

    public CartArticleJPAEntity(long articleId, int quantity) {
        this.articleId = articleId;
        this.quantity = quantity;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
