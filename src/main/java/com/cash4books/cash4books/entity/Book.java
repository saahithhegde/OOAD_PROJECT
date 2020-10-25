package com.cash4books.cash4books.entity;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer bookID;

    String userId;
    String title;
    String author;
    double price;
    String category;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
