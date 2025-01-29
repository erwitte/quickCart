package de.hsos.user.boundary.DTO;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class CreateUserDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String city;
    private String zipCode;
    private String houseNumber;
    private String currency;

    public CreateUserDTO() {}

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
            @JsonbProperty("zipCode") String zipCode,
            @JsonbProperty("currency") String currency
    ) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.zipCode = zipCode;
        this.currency = currency;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
