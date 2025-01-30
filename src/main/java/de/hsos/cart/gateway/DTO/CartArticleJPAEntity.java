package de.hsos.cart.gateway.DTO;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity(name = "cart_article")
public class CartArticleJPAEntity extends PanacheEntity {
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
