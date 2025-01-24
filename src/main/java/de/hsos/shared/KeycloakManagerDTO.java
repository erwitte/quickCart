package de.hsos.shared;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KeycloakManagerDTO {

    private final String clientId = "quarkus_be";
    private final String clientSecret = "Ffw0GGjHrfTFnLb8mHFlBGLjcIL8hpfO";
    private final String grantType = "password";
    private final String username = "sagar";
    private final String password = "demo";

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

    public KeycloakManagerDTO() {
    }
}
