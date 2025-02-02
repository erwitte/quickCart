package de.hsos.article.gateway.DTO;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "rating")
@Table(name = "RATING")
public class RatingJPAEntity extends PanacheEntity {
    public String username;
    public String review;
    public int rating;

    public RatingJPAEntity() {}

    public RatingJPAEntity(String username, String review, int rating) {
        this.username = username;
        this.review = review;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
