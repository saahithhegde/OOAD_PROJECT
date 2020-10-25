package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BookServiceImpl {
@Autowired
private BookRepository bookRepository;

    public void addBook(Book book, HttpServletRequest request) throws Exception {
        String email = (String) request.getSession().getAttribute("USER_SESSION_ATTRIBUTES");
        //TODO custom exception
        if(email==null ||  email.equals(""))
            throw new Exception("User not logged in");
        book.setUserId(email);
        bookRepository.save(book);
    }
}
