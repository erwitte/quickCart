package de.hsos.users.boundary;

import de.hsos.shared.CreateUserDTO;
import de.hsos.shared.KeycloakAPI;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/users")
@RequestScoped
@Authenticated
public class UsersResource {
    @Inject
    Template authenticate;
    @Inject
    @RestClient
    KeycloakAPI keycloakAPI;

    @POST
    @Path("/e")
    @PermitAll
    public Response getUsers(CreateUserDTO newUser, @Context HttpHeaders headers) {
        String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Missing or invalid Authorization header").build();
        }
        
        keycloakAPI.createUser(newUser, authorizationHeader);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public Response authenticate(){
        TemplateInstance renderedTemplate = authenticate.instance();
        return Response.ok(renderedTemplate.render()).type(MediaType.TEXT_HTML).build();
    }
}
