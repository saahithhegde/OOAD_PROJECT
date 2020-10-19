package com.example.cash4books.cash4books.service;

import com.example.cash4books.cash4books.entity.Book;
import com.example.cash4books.cash4books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookService {
@Autowired
private BookRepository bookRepository;

    public void addBook(Book book, String userId){
        book.setUserId(userId);
        bookRepository.save(book);
    }
}
