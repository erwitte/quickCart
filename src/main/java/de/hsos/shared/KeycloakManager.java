package de.hsos.shared;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RequestScoped
public class KeycloakManager {

    private final String clientId = "quarkus-be";
    private final String clientSecret = "Ffw0GGjHrfTFnLb8mHFlBGLjcIL8hpfO";
    private final String grantType = "password";
    private final String username = "keycloak_manager";
    private final String password = "password";
    private ReceiveToken token;
    @Inject
    @RestClient
    KeycloakAPI keycloakAPI;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public KeycloakManager() {
    }

    public String getAccessToken() {
        Response response = keycloakAPI.receiveToken(clientId, clientSecret, grantType, username, password);
        this.token = response.readEntity(ReceiveToken.class);
        return token.getAccess_token();
    }

    public void logOutKeycloakManager(){
        keycloakAPI.logoutKeycloakManager(clientId, clientSecret, token.getRefresh_token());
    }
}
