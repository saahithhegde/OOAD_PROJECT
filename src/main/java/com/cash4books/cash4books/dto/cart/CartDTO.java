package com.cash4books.cash4books.dto.cart;

import com.cash4books.cash4books.entity.Book;

public class CartDTO{
    private String email;

    private Integer bookID;

    private Book bookDetails;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public Book getBookDetails() {
        return bookDetails;
    }

    public void setBookDetails(Book bookDetails) {
        this.bookDetails = bookDetails;
    }
}

