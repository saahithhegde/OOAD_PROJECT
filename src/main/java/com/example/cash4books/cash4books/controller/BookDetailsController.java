package com.example.cash4books.cash4books.controller;

import com.example.cash4books.cash4books.dto.BookDto;
import com.example.cash4books.cash4books.entity.Book;
import com.example.cash4books.cash4books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class BookDetailsController {
    @Autowired
    BookService bookService;
    @PostMapping(value = "/addBook", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDto addBook(@RequestBody Book book , HttpSession session){
           String userId = (String) session.getAttribute("SESSION_ID");
           bookService.addBook(book,userId);
           return new BookDto();

    }
}
