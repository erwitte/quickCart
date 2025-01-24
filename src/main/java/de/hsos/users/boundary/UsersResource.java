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

    @GET
    @Path("/e")
    @PermitAll
    public Response getUsers() {
        //keycloakAPI.createUser(new CreateUserDTO("name", "af@aofj.de", "spgf", "üodsg", "posjf 1"), token);
        CreateUserDTO a = new CreateUserDTO("ellich", "pw", "firstName", "lastName", "aasdeasdafdffa@afa.de");
        a.setAddress("street", "city", "state", "zip");
        return Response.ok(a).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response authenticate(){
        TemplateInstance renderedTemplate = authenticate.instance();
        return Response.ok(renderedTemplate.render()).type(MediaType.TEXT_HTML).build();
    }
}
