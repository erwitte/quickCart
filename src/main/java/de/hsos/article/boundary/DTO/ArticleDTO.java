package de.hsos.article.boundary.DTO;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;


public class ArticleDTO {
    private String heading;
    private double price;

    public ArticleDTO() {
    }

    @JsonbCreator
    public ArticleDTO(@JsonbProperty("id") String heading,
                      @JsonbProperty("price") double price) {
        this.heading = heading;
        this.price = price;
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
}