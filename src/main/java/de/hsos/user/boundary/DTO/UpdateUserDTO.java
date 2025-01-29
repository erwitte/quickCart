package de.hsos.user.boundary.DTO;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class UpdateUserDTO {

    private String street;

    private String houseNumber;

    private String zipCode;

    private String city;

    private String currency; // Can be null if no change is selected

    // Default Constructor (Needed for JSON deserialization)
    public UpdateUserDTO() {}

    @JsonbCreator
    public UpdateUserDTO(@JsonbProperty("street")String street,
                         @JsonbProperty("houseNumber") String houseNumber,
                         @JsonbProperty("zipCode") String zipCode,
                         @JsonbProperty("city") String city,
                         @JsonbProperty("currency") String currency) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.currency = currency;
    }

    // Getters and Setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
