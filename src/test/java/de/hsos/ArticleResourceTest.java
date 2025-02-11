package de.hsos;

import de.hsos.article.boundary.ArticleResource;
import de.hsos.article.boundary.ArticleResourceFallback;
import de.hsos.article.boundary.DTO.ArticleDTO;
import de.hsos.article.boundary.DTO.ReceiveRatingDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ArticleResourceTest {

    @Inject
    ArticleResource articleResource;

    @Inject
    ArticleResourceFallback fallback;

    @Test
    public void testFallbackSavePicture() {
        Response response = fallback.fallbackSavePicture(1L, new byte[]{});
        assertEquals(503, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Unable to upload image at the moment"));
    }

    @Test
    public void testFallbackSaveArticle() {
        ArticleDTO articleDTO = new ArticleDTO("Test Article", 10.0);
        Response response = fallback.fallbackSaveArticle(articleDTO);
        assertEquals(503, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Unable to create article at the moment"));
    }

    @Test
    public void testFallbackGetAllArticles() {
        Response response = fallback.fallbackGetAllArticles();
        assertEquals(503, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Unable to retrieve articles at the moment"));
    }

    @Test
    public void testFallbackGetArticle() {
        Response response = fallback.fallbackGetArticle(1L);
        assertEquals(503, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Unable to retrieve article at the moment"));
    }

    @Test
    public void testFallbackGetArticleDetail() {
        Response response = fallback.fallbackGetArticleDetail(1L);
        assertEquals(503, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Unable to retrieve article details at the moment"));
    }

    @Test
    public void testFallbackAddRating() {
        ReceiveRatingDTO receiveRatingDTO = new ReceiveRatingDTO("Great product!", 5);
        assertDoesNotThrow(() -> fallback.fallbackAddRating(1L, receiveRatingDTO));
    }

    @Test
    public void testFallbackDeleteArticle() {
        assertDoesNotThrow(() -> fallback.fallbackDeleteArticle(1L));
    }
}
