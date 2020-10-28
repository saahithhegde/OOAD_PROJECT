package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.BookRepository;
import com.cash4books.cash4books.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class BookServiceImpl {
@Autowired
private BookRepository bookRepository;

@Autowired
private UserRepository userRepository;

    public Book addBook(Book book, HttpServletRequest request) throws Exception {
        String email = (String) request.getSession().getAttribute("USER_SESSION_ATTRIBUTES");
        //TODO custom exception
        if(email==null ||  email.equals(""))
            throw new Exception("User not logged in");
        Users user =  userRepository.findUserByEmail(email);
        book.setUsers(user);
        book = bookRepository.save(book);
        return book;
    }

    public List<Book> filterByAuthor(String author){
        List<Book> books = bookRepository.findAllByAuthor(author);
        return books;
    }

    public List<Book> getBooksWithTitle(String title){
        List<Book> books = bookRepository.findByTitleContaining(title);
        return books;
    }

    public List<Book> filterByCategory(String category){
        List<Book> books = bookRepository.findAllByCategory(category);
        return books;
    }

    public List<Book> getBooksBySeller(String id) {
        Users user = userRepository.findUserByEmail(id);
        List<Book> books = bookRepository.findAllByUsers(user);
        return books;
    }
}
