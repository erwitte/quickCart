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
import org.eclipse.microprofile.faulttolerance.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@RequestScoped
@Path("/cart")
@RolesAllowed("user")
@Tag(name = "Cart", description = "Operations related to the user's shopping cart.")
public class CartResource {
    @Inject
    CartService cartService;
    @Inject
    JsonWebToken jwt;
    @Inject
    Template checkoutUser;

    @POST
    @Operation(
            summary = "Create a new cart",
            description = "Creates a new shopping cart for the authenticated user if it does not already exist."
    )
    @Retry(maxRetries = 3, delay = 500) // Retries 3 times with 500ms delay between attempts
    @Timeout(2000) // Timeout after 2 seconds
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000) // Breaker trips if 50% of 4 requests fail, waits 5s
    public void createCart() {
        String username = jwt.getClaim("preferred_username");
        if (!cartService.cartExists(username)){
            cartService.createCart(username);
        }
    }

    @PATCH
    @Operation(
            summary = "Add an article to the cart",
            description = "Adds a specified quantity of an article to the user's shopping cart."
    )
    @Retry(maxRetries = 3, delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    public void addToCart(@RequestBody(
            description = "Details of the article to add",
            required = true,
            content = @Content(schema = @Schema(implementation = AddArticleDTO.class))
    )
                          AddArticleDTO addArticleDTO) {
        String username = jwt.getClaim("preferred_username");
        cartService.addArticleToCart(username, addArticleDTO.articleId(), addArticleDTO.quantity());
    }

    private Cart getCart(){
        String username = jwt.getClaim("preferred_username");
        return cartService.getCart(username);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Operation(
            summary = "Get checkout page",
            description = "Returns the HTML checkout page with a list of articles in the user's cart."
    )
    @APIResponse(
            responseCode = "200",
            description = "Checkout page returned",
            content = @Content(mediaType = MediaType.TEXT_HTML)
    )
    @APIResponse(responseCode = "401", description = "Unauthorized, only users can access this")
    @Retry(maxRetries = 3, delay = 500)
    @Timeout(3000) // Slightly longer timeout for loading UI elements
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    public Response getCheckoutpage(){
        List<ArticleCartDTO> articlesInCart = getCart().articleIdAndQuantity().entrySet().stream()
                .map(entry -> new ArticleCartDTO(entry.getKey(), entry.getValue()))
                .toList();
        TemplateInstance checkOutInstance = checkoutUser.data("articles", articlesInCart);
        return Response.ok(checkOutInstance).build();
    }

    @POST
    @Path("/checkout")
    @Operation(
            summary = "Checkout the cart",
            description = "Finalizes the shopping cart, processing the order."
    )
    @Retry(maxRetries = 3, delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    public void checkOut(){
        String username = jwt.getClaim("preferred_username");
        cartService.checkoutCart(username);
    }
}
