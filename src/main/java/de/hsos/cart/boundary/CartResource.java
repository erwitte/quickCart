package de.hsos.cart.boundary;

import de.hsos.cart.boundary.DTO.AddArticleDTO;
import de.hsos.cart.boundary.DTO.ArticleCartDTO;
import de.hsos.cart.cotrol.CartService;
import de.hsos.cart.entity.Cart;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/cart")
@RolesAllowed("user")
public class CartResource {
    @Inject
    CartService cartService;
    @Inject
    JsonWebToken jwt;
    @Inject
    Template checkOutUser;

    @POST
    public void createCart() {
        String username = jwt.getClaim("preferred_username");
        if (cartService.cartExists(username)){
            cartService.createCart(username);
        }
    }

    @PATCH
    public void addToCart(AddArticleDTO addArticleDTO) {
        String username = jwt.getClaim("preferred_username");
        cartService.addArticleToCart(username, addArticleDTO.articleId(), addArticleDTO.quantity());
    }

    private Cart getCart(){
        String username = jwt.getClaim("preferred_username");
        return cartService.getCart(username);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getCheckOutpage(){
        List<ArticleCartDTO> articlesInCart = getCart().articleIdAndQuantity().entrySet().stream()
                .map(entry -> {
                    long articleId = entry.getKey();
                    int quantity = entry.getValue();
                    return new ArticleCartDTO(articleId, quantity);
                })
                .toList();
        TemplateInstance checkOutInstance = checkOutUser.data("articles", articlesInCart);
       return Response.ok(checkOutInstance).build();
    }

    @POST
    @Path("/checkout")
    public void checkOut(){
        String username = jwt.getClaim("preferred_username");
        cartService.checkoutCart(username);
    }
}
