package de.hsos.article.gateway.DTO;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity(name = "article")
@Table(name = "ARTICLE")
public class ArticleJPAEntity extends PanacheEntity {
    public String heading;
    public double price;
    @Lob
    public byte[] picture;

    public ArticleJPAEntity() {}

    public ArticleJPAEntity(String heading, double price, byte[] picture) {
        this.heading = heading;
        this.price = price;
        this.picture = picture;
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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
