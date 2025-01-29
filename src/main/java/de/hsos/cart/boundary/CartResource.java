package de.hsos.cart.boundary;

import de.hsos.cart.boundary.DTO.ArticleDTO;
import de.hsos.cart.cotrol.CartService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
@Path("/cart")
@RolesAllowed("user")
public class CartResource {
    @Inject
    CartService cartService;
    @Inject
    JsonWebToken jwt;

    @POST
    public void createCart() {
        String username = jwt.getClaim("preferred_username");
        if (cartService.cartExists(username)){
            cartService.createCart(username);
        }
    }

    @PATCH
    public void addToCart(ArticleDTO articleDTO) {
        String username = jwt.getClaim("preferred_username");
        cartService.addArticleToCart(username, articleDTO.articleId(), articleDTO.quantity());
    }
}
