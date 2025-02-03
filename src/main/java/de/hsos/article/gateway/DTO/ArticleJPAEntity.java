package de.hsos.article.gateway.DTO;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "article")
@Table(name = "ARTICLE")
public class ArticleJPAEntity extends PanacheEntity {
    public String heading;
    public double price;
    @Lob
    public byte[] image;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<RatingJPAEntity> ratings = new ArrayList<>();

    public ArticleJPAEntity() {}

    public ArticleJPAEntity(String heading, double price, byte[] image, List<RatingJPAEntity> ratings) {
        this.heading = heading;
        this.price = price;
        this.image = image;
        this.ratings = ratings;
    }

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<RatingJPAEntity> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingJPAEntity> ratings) {
        this.ratings = ratings;
    }
}
