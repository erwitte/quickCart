package de.hsos.users.entity;

import de.hsos.users.boundary.DTO.CreateUserDTO;

public class User {
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

    public User(
            String username, String password, String firstName, String lastName,
            String email, String street, String city, String zipCode,
            String houseNumber, String currency
    ) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
        this.currency = currency;
    }

    public User(CreateUserDTO createUserDTO) {
        this.username = createUserDTO.getUsername();
        this.password = createUserDTO.getPassword();
        this.firstName = createUserDTO.getFirstName();
        this.lastName = createUserDTO.getLastName();
        this.email = createUserDTO.getEmail();
        this.street = createUserDTO.getStreet();
        this.city = createUserDTO.getCity();
        this.zipCode = createUserDTO.getZipCode();
        this.houseNumber = createUserDTO.getHouseNumber();
        this.currency = createUserDTO.getCurrency();
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