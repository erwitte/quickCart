package de.hsos.cart.cotrol;

public interface CartService {
    void addArticleToCart(String username, long articleId, int quantity);
    void createCart(String username);
    boolean cartExists(String username);
}
