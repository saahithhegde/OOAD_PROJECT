package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.services.impl.BookServiceImpl;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookDetailsController {

    Logger logger = LoggerFactory.getLogger(BookDetailsController.class);

    @Autowired
    BookServiceImpl bookServiceImpl;


    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addBook(@RequestBody Book book , HttpServletRequest request){
        try {
            book = bookServiceImpl.addBook(book,request);
        } catch (Exception e) {
            logger.error("Failed to add book, user is not logged in");
            return new ResponseEntity("User not logged in", HttpStatus.BAD_REQUEST);
        }
        logger.info("Successfully added book:"+book.getTitle());
        return new ResponseEntity(book, HttpStatus.ACCEPTED);

    }

    @GetMapping(path = "/seller/{sellerID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> searchSeller(@PathVariable("sellerID") String id){
        List<Book> books = bookServiceImpl.getBooksBySeller(id);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping(path = "/author/{author}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> searchByAuthor(@PathVariable("author") String author){
        List<Book> books = bookServiceImpl.filterByAuthor(author);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping(path = "/title/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> searchByTitle(@PathVariable("title") String title){
        List<Book> books = bookServiceImpl.getBooksWithTitle(title);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping(path = "/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> searchByCategory(@PathVariable("category") String category){
        List<Book> books = bookServiceImpl.getBooksWithTitle(category);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }
}
