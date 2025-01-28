package de.hsos.article.control;

public interface CartService {
    void addArticleToCart(String username, long articleId, int quantity);
    void createCart(String username);
}
