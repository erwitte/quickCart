package de.hsos.shared;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class CreateUserDTO {

    private List<CredentialDTO> credentials;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified = true;
    private Map<String, String> attributes = new HashMap<>();
    private boolean enabled = true; // Always true

    // No-arg constructor for JSON-B
    public CreateUserDTO() {
    }

    @JsonbCreator
    public CreateUserDTO(
            @JsonbProperty("username") String username,
            @JsonbProperty("password") String password,
            @JsonbProperty("firstName") String firstName,
            @JsonbProperty("lastName") String lastName,
            @JsonbProperty("email") String email,
            @JsonbProperty("street") String street,
            @JsonbProperty("houseNumber") String houseNumber,
            @JsonbProperty("city") String city,
            @JsonbProperty("zipCode") String zipCode) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        setAddress(street, houseNumber, city, zipCode);
        this.credentials = List.of(new CredentialDTO(password));
    }

    public void setAddress(String street, String houseNumber, String city, String zipCode) {
        this.attributes.put("street", street);
        this.attributes.put("house_number", houseNumber);
        this.attributes.put("city", city);
        this.attributes.put("zip_code", zipCode);
    }

    public Map<String, String> getAttributes() {
        return attributes;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // Nested class for Credentials
    public static class CredentialDTO {
        private boolean temporary = false; // Always false
        private String type = "password"; // Always "password"
        private String value;

        // No-arg constructor for JSON-B
        public CredentialDTO() {
        }

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

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
