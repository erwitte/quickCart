package de.hsos.shared;

import de.hsos.user.boundary.DTO.CreateUserKeycloakDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "keycloakAPI")
public interface KeycloakAPI {

    @POST
    @Path("/admin/realms/quickcart-realm/users")
    @Consumes(MediaType.APPLICATION_JSON)
    Response createUser(CreateUserKeycloakDTO newUser, @HeaderParam("Authorization") String bearerToken);

    @POST
    @Path("/realms/quickcart-realm/protocol/openid-connect/token")
    Response receiveToken(@FormParam("client_id") String client_id,
                              @FormParam("client_secret") String client_secret,
                              @FormParam("grant_type") String grant_type,
                              @FormParam("username") String username,
                              @FormParam("password") String password);

    @POST
    @Path("/realms/quickcart-realm/protocol/openid-connect/logout")
    void logout(@FormParam("client_id") String client_id,
                @FormParam("client_secret") String client_secret,
                @FormParam("refresh_token") String refresh_token);

    default void logout(String refresh_token) {
        logout("quarkus-be", "Ffw0GGjHrfTFnLb8mHFlBGLjcIL8hpfO", refresh_token);
    }
}
