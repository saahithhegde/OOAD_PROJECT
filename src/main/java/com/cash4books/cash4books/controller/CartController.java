package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.services.impl.BookServiceImpl;
import com.cash4books.cash4books.services.impl.CartServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    Logger logger = LoggerFactory.getLogger(BookDetailsController.class);

    @Autowired
    CartServiceImpl cartServiceImpl;


//    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Book> addBook(@RequestBody Book book , HttpServletRequest request, @RequestHeader(name = "Token") String token){
//        try {
//            book = cartServiceImpl.addToCart(book,request,token);
//        } catch (Exception e) {
//            logger.error("Failed to add book to cart, user is not logged in");
//            return new ResponseEntity("User not logged in", HttpStatus.BAD_REQUEST);
//        }
//        logger.info("Successfully added book to cart:"+book.getTitle());
//        return new ResponseEntity(book, HttpStatus.ACCEPTED);
//      }
}
