package de.hsos.article.boundary.DTO;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.Base64;

public class ArticleDTO {
    private String heading;
    private double price;
    private byte[] picture;

    // Default constructor (required for JSON-B deserialization)
    public ArticleDTO() {
    }

    @JsonbCreator
    public ArticleDTO(@JsonbProperty("heading") String heading,
                      @JsonbProperty("price") double price,
                      @JsonbProperty("picture") byte[] picture) {
        this.heading = heading;
        this.price = price;
        this.picture = picture;
    }

    // Getters and setters (required for JSON-B serialization/deserialization)
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}