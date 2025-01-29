package de.hsos.user.boundary.DTO;

import java.util.List;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class CreateUserKeycloakDTO {

    private List<CredentialDTO> credentials;
    private String username;
    private String email;
    private boolean emailVerified = true;
    private boolean enabled = true; // Always true

    // Constructor for JSON-B with required fields
    @JsonbCreator
    public CreateUserKeycloakDTO(
            @JsonbProperty("username") String username,
            @JsonbProperty("password") String password,
            @JsonbProperty("email") String email) {
        this.username = username;
        this.email = email;
        this.credentials = List.of(new CredentialDTO(password));
    }

    // Getters and Setters
    public List<CredentialDTO> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialDTO> credentials) {
        this.credentials = credentials;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    // Nested class for Credentials
    public static class CredentialDTO {
        private boolean temporary = false; // Always false
        private String type = "password"; // Always "password"
        private String value;

        // Constructor for JSON-B
        public CredentialDTO(String value) {
            this.value = value;
        }

        // Getters and Setters
        public boolean isTemporary() {
            return temporary;
        }

        public void setTemporary(boolean temporary) {
            this.temporary = temporary;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
