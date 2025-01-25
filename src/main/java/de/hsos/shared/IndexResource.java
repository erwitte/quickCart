package de.hsos.shared;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RequestScoped
@Path("/")
public class IndexResource {
    @Inject
    Template index;
    @Inject
    Template register;
    @Inject
    @RestClient
    KeycloakAPI keycloakAPI;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response index() {
        TemplateInstance indexInstance = index.instance();
        return Response.ok(indexInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("register")
    public Response register() {
        TemplateInstance registerInstance = register.instance();
        return Response.ok(registerInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    @POST
    @Path("login")
    public Response login(LoginDTO loginDTO) {
        return keycloakAPI.receiveToken(
                "quarkus-be",
                "Ffw0GGjHrfTFnLb8mHFlBGLjcIL8hpfO",
                "password",
                loginDTO.username(),
                loginDTO.password()
        );
    }
}
