package com.cash4books.cash4books.dto.book;

public class BookDtoQuery {
    private String isbn;
    private String title;
    private Long count;
    private String author;
    private String category;
    private String description;


    public BookDtoQuery(String isbn, String title, String author, String category, String description, Long count) {
        this.isbn = isbn;
        this.title = title;
        this.count = count;
        this.author = author;
        this.category = category;
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
