package de.hsos.shared;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "keycloakAPI")
public interface KeycloakAPI {

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    Response createUser(CreateUserDTO newUser, @HeaderParam("Authorization") String bearerToken);
}
