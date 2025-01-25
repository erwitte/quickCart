package de.hsos.users.boundary;

import de.hsos.shared.CreateUserDTO;
import de.hsos.shared.KeycloakAPI;
import de.hsos.shared.KeycloakManager;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
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
public class UsersResource {
    @Inject
    Template authenticate;
    @Inject
    @RestClient
    KeycloakAPI keycloakAPI;
    @Inject
    KeycloakManager keycloakManager;

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

    @POST
    @Path("/est")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response est() {
        //ReceiveToken sa = keycloakAPI.receiveToken("quarkus-be", "Ffw0GGjHrfTFnLb8mHFlBGLjcIL8hpfO", "password", "keycloak_manager", "password");
        System.out.println(keycloakManager.getAccessToken());
        /*String token = sa.getAccess_token();
        String[] parts = token.split("\\."); // Split token into parts

        // Decode the Payload (2nd part)
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

        // Parse and pretty-print the JSON
        ObjectMapper mapper1 = new ObjectMapper();
        Object json = mapper1.readValue(payload, Object.class);
        String prettyJson = mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(json);

        System.out.println(prettyJson);*/
        return Response.ok().build();
    }

    @POST
    @PermitAll
    public Response createUser(CreateUserDTO newUser) {
        String keycloakManagerAccessToken = "Bearer " + keycloakManager.getAccessToken();
        Response wasCreated = keycloakAPI.createUser(newUser, keycloakManagerAccessToken);
        if (wasCreated.getStatus() == 201){
            keycloakManager.logOutKeycloakManager();
        }
        return wasCreated;
    }
}
