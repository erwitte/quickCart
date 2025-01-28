package de.hsos.article.gateway.DTO;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "cart")
@Table(name = "CART")
public class CartJPAEntity extends PanacheEntity {
    public String username;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CartArticleJPAEntity> articles;

    public CartJPAEntity() {
        articles = new ArrayList<>();
    }

    public CartJPAEntity(String username) {
        this.username = username;
        this.articles = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CartArticleJPAEntity> getArticles() {
        return articles;
    }

    public void setArticles(List<CartArticleJPAEntity> articles) {
        this.articles = articles;
    }
}
