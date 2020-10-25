package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.services.impl.BookServiceImpl;
import com.cash4books.cash4books.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class BookDetailsController {

    Logger logger = LoggerFactory.getLogger(BookDetailsController.class);

    @Autowired
    BookServiceImpl bookServiceImpl;
    @PostMapping(value = "/addBook", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addBook(@RequestBody Book book , HttpServletRequest request){
        try {
            bookServiceImpl.addBook(book,request);
        } catch (Exception e) {
            logger.error("Failed to add book, user is not logged in");
            return new ResponseEntity("User not logged in", HttpStatus.BAD_REQUEST);
        }
        logger.info("Successfully added book:"+book.getTitle());
        return new ResponseEntity(book, HttpStatus.ACCEPTED);

    }
}
