package de.hsos.admin.boundary;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AdminResourceFallback {

    public Response fallbackIndexAdmin(int page, int pageSize) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Admin dashboard is currently unavailable. Please try again later.")
                .build();
    }

    public Response fallbackFileUploadPage() {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("File upload page is currently unavailable. Please try again later.")
                .build();
    }
}

