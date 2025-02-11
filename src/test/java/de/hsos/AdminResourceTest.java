package de.hsos;

import de.hsos.admin.boundary.AdminResource;
import de.hsos.admin.boundary.AdminResourceFallback;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
