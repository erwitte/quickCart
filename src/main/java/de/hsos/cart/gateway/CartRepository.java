package de.hsos.cart.gateway;

import de.hsos.cart.cotrol.CartService;
import de.hsos.cart.gateway.DTO.CartArticleJPAEntity;
import de.hsos.cart.gateway.DTO.CartJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class CartRepository implements CartService, PanacheRepository<CartJPAEntity> {

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
        return find("username", username) != null;
    }

    @Override
    @Transactional
    public void createCart(String username){
        CartJPAEntity cartEntity = new CartJPAEntity(username);
        cartEntity.persist();
    }
}
