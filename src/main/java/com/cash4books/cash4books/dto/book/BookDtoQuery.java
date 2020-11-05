package com.cash4books.cash4books.dto.book;

public class BookDtoQuery {
    private String isbn;
    private String title;
    private Long count;

    public BookDtoQuery(String isbn, String title, Long count) {
        this.isbn = isbn;
        this.title = title;
        this.count = count;
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
}
