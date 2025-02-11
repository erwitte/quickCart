package de.hsos;

import de.hsos.article.boundary.DTO.ArticleDTO;
import de.hsos.article.control.ArticleService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@QuarkusTest
public class ArticleResourceIT {

    @InjectMock
    ArticleService articleService;

    @BeforeEach
    void setup() {
        // Ensure REST Assured uses the correct base path
        RestAssured.basePath = "/articles";
    }

    @Test
    @TestSecurity(user="admin", roles="admin")
    public void testFallbackSaveArticle() {
        // Simulate a failure in the service
        doThrow(new RuntimeException("Simulated Service Failure")).when(articleService).createArticle(Mockito.any());

        // Call the API and expect fallback
        given()
                .contentType(APPLICATION_JSON)
                .body(new ArticleDTO("Fallback Article", 99.99))
                .when()
                .post("/")
                .then()
                .statusCode(503)
                .body(containsString("Unable to create article at the moment"));
    }

    @Test
    @TestSecurity(user="user", roles="user")
    public void testFallbackGetArticle() {
        // Simulate a failure in the service
        doThrow(new RuntimeException("Simulated Service Failure")).when(articleService).getArticle(1L);

        // Call the API and expect fallback
        given()
                .when()
                .get("/1")
                .then()
                .statusCode(503)
                .body(containsString("Unable to retrieve article at the moment"));
    }

    @Test
    @TestSecurity(user="admin", roles="admin")
    public void testFallbackDeleteArticle() {
        // Simulate a failure in the service
        doThrow(new RuntimeException("Simulated Service Failure")).when(articleService).deleteArticle(1L);

        given()
                .when()
                .delete("/1")
                .then()
                .statusCode(503)
                .body(containsString("Failed to delete article. Please try again later."));
    }
}

