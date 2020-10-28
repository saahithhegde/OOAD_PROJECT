package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.BookRepository;
import com.cash4books.cash4books.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class BookServiceImpl {
@Autowired
private BookRepository bookRepository;

@Autowired
private UserRepository userRepository;

@Autowired
SessionServiceImpl sessionService;

    public Book addBook(Book book, HttpServletRequest request, String token, String email ) throws Exception {
        //TODO custom exception
        if(sessionService.getSessionValidation(request,token,email)){
            if(email==null ||  email.equals(""))
                throw new Exception("User not logged in");
            Users user =  userRepository.findUserByEmail(email);
            book.setUsers(user);
            book = bookRepository.save(book);
            return book;
        } else throw new Exception("User not logged in");

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
