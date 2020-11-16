package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.book.BookDto;
import com.cash4books.cash4books.dto.book.BookDtoQuery;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.exception.UserNotLoggedInException;
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

    public Book addBook(Book book, HttpServletRequest request, String token ) throws Exception {
        //TODO custom exception
        String email = sessionService.getSessionValidation(request,token);
        if(email==null ||  email.equals(""))
                throw new Exception("User not logged in");
            Users user =  userRepository.findUserByEmail(email);
            book.setUsers(user);
            book = bookRepository.save(book);
            return book;

    }

    public List<Book> getBooksByIsbn(String isbn){
        List<Book> books = bookRepository.findAllByIsbn(isbn);
        return books;
    }

    public List<BookDtoQuery> fetchAllDistinctIsbn(){
        List<BookDtoQuery>books=bookRepository.fetchAllDisticntIsbn();
        return books;
    }

    public List<Book> searchByAny(String keyword){
        if(keyword!=null){
            List<Book> books = bookRepository.search(keyword);
            return books;
        }
        return (List<Book>) bookRepository.findAll();
    }

    public Book getBookById(Integer Id){
        Book book=bookRepository.findByBookID(Id);
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
        //Users user = userRepository.findUserByEmail(id);
        List<Book> books = bookRepository.findAllByEmailID(id);
        return books;
    }

    public List<Book> getBooksOfSeller(HttpServletRequest request, String token ) throws Exception {
        String email = sessionService.getSessionValidation(request,token);
        if(email==null ||  email.equals(""))
            throw new UserNotLoggedInException();
        Users user = userRepository.findUserByEmail(email);
        List<Book> books = bookRepository.findAllByUsers(user);
        return books;
    }

    public Book getUserBook(Integer bookId,Users users){
        return bookRepository.findBookByBookIDAndUsers(bookId,users);
    }
}
