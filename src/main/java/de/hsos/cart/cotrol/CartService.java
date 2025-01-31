package de.hsos.cart.cotrol;

import de.hsos.cart.entity.Cart;

public interface CartService {
    void addArticleToCart(String username, long articleId, int quantity);
    void createCart(String username);
    boolean cartExists(String username);
    Cart getCart(String username);
    void checkoutCart(String username);
}
