package de.hsos.cart.gateway;

import de.hsos.cart.cotrol.CartService;
import de.hsos.cart.entity.Cart;
import de.hsos.cart.gateway.DTO.CartArticleJPAEntity;
import de.hsos.cart.gateway.DTO.CartJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequestScoped
public class CartRepository implements CartService, PanacheRepository<CartJPAEntity> {

    @Override
    @Transactional
    public void checkoutCart(String username){
        CartJPAEntity cartJPAEntity = find("username", username).firstResult();
        for (CartArticleJPAEntity article : cartJPAEntity.getArticles()) {
            article.delete();  // Panache way to delete entity
        }

        cartJPAEntity.getArticles().clear();
        cartJPAEntity.persist();
    }

    @Override
    @Transactional
    public void addArticleToCart(String username, long articleId, int quantity){
        CartJPAEntity cartJPAEntity = find("username", username).firstResult();
        List<CartArticleJPAEntity> cart = cartJPAEntity.getArticles();
        CartArticleJPAEntity cartArticle = new CartArticleJPAEntity(articleId, quantity);
        cart.add(cartArticle);
        cartJPAEntity.setArticles(cart);
        cartJPAEntity.persist();
    }

    @Override
    public boolean cartExists(String username){
        return find("username", username) == null;
    }

    @Override
    @Transactional
    public void createCart(String username){
        CartJPAEntity cartEntity = new CartJPAEntity(username);
        cartEntity.persist();
    }

    @Override
    public Cart getCart(String username){
        CartJPAEntity cartEntity = find("username", username).firstResult();
        List<CartArticleJPAEntity> cartArticles = cartEntity.getArticles();
        Cart cart = new Cart(new HashMap<>());
        for (CartArticleJPAEntity cartArticle : cartArticles) {
            cart.articleIdAndQuantity().put(cartArticle.getArticleId(), cartArticle.getQuantity());
        }
        return cart;
    }
}
