package de.hsos;

import de.hsos.acl.ArticleServiceAdapter;
import de.hsos.admin.boundary.AdminResource;
import de.hsos.admin.boundary.AdminResourceFallback;
import de.hsos.shared.ArticleDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class AdminResourceTest {
    @Inject
    AdminResource adminResource;

    @Inject
    AdminResourceFallback fallback;

    @Test
    public void testFallbackIndexAdmin() {
        // Call the fallback method
        Response response = fallback.fallbackIndexAdmin(1, 9);
        assertEquals(503, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Admin dashboard is currently unavailable"));
    }

    @Test
    public void testFallbackFileUploadPage() {
        // Simulate an error scenario
        Response response = fallback.fallbackFileUploadPage();
        assertEquals(503, response.getStatus());
        assertTrue(response.getEntity().toString().contains("File upload page is currently unavailable"));
    }
}
