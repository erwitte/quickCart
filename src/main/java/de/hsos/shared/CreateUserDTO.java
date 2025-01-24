package de.hsos.shared;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateUserDTO {
    private final List<CredentialDTO> credentials;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified = true;
    private final Map<String, String> attributes = new HashMap<>();
    private final boolean enabled = true; // Always true

    public CreateUserDTO(String username, String password, String firstName, String lastName, String email ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        // Default credentials
        this.credentials = List.of(new CredentialDTO(password));
    }

    public void setAddress(String street, String houseNumber, String city, String zip_code) {
        this.attributes.put("street", street);
        this.attributes.put("house_number", houseNumber);
        this.attributes.put("city", city);
        this.attributes.put("zip_code", zip_code);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    // Getters and Setters
    public List<CredentialDTO> getCredentials() {
        return credentials;
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

    // Nested class for Credentials
    public static class CredentialDTO {
        private final boolean temporary = false; // Always false
        private final String type = "password"; // Always "password"
        private String value;

        public CredentialDTO(String value) {
            this.value = value;
        }

        // Getters
        public boolean isTemporary() {
            return temporary;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        // Setter for value
        public void setValue(String value) {
            this.value = value;
        }
    }
}
