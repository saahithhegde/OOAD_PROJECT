package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.BookRepository;
import com.cash4books.cash4books.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BookServiceImpl {
@Autowired
private BookRepository bookRepository;

@Autowired
private UserRepository userRepository;

    public void addBook(Book book, HttpServletRequest request) throws Exception {
        String email = (String) request.getSession().getAttribute("USER_SESSION_ATTRIBUTES");
        //TODO custom exception
        if(email==null ||  email.equals(""))
            throw new Exception("User not logged in");
        Users user =  userRepository.findUserByEmail(email);
        book.setUsers(user);
        bookRepository.save(book);
    }
}
