package de.hsos.cart.gateway;

import de.hsos.article.control.ArticleService;
import de.hsos.cart.cotrol.CartService;
import de.hsos.cart.gateway.DTO.CartArticleJPAEntity;
import de.hsos.cart.gateway.DTO.CartJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class CartRepository implements CartService, PanacheRepository<CartJPAEntity> {
    @Inject
    ArticleService articleService;

    @Override
    @Transactional
    public void addArticleToCart(String username, long articleId, int quantity){
        CartJPAEntity cartJPAEntity = (CartJPAEntity) find("username", username);
        List<CartArticleJPAEntity> cart = cartJPAEntity.getArticles();
        CartArticleJPAEntity cartArticle = new CartArticleJPAEntity(articleService.getJPAArticle(articleId), quantity);
        cart.add(cartArticle);
        cartJPAEntity.setArticles(cart);
        cartJPAEntity.persist();
    }

    @Override
    @Transactional
    public void createCart(String username){
        CartJPAEntity cartEntity = new CartJPAEntity(username);
        cartEntity.persist();
    }
}
