package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.book.BookDtoQuery;
import com.cash4books.cash4books.services.impl.BookServiceImpl;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookDetailsController {

    Logger logger = LoggerFactory.getLogger(BookDetailsController.class);

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("");
    }

    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000000);
        return multipartResolver;
    }

    @Autowired
    BookServiceImpl bookServiceImpl;

    @PostMapping(value = "/add", headers="Content-Type=multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addBook( @RequestParam(value = "file",required = false) MultipartFile file, @RequestParam(value = "book") String bookJson , HttpServletRequest request, @RequestHeader(name = "Token") String token){
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        Book book = null;
        try {
            book = objectMapper.readValue(bookJson,Book.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        try {
            book.setImage(file.getBytes());
            book = bookServiceImpl.addBook(book,request,token);
        } catch (Exception e) {
            logger.error("Failed to add book, user is not logged in");
            return new ResponseEntity("User not logged in", HttpStatus.BAD_REQUEST);
        }
        logger.info("Successfully added book:"+book.getTitle());
        return new ResponseEntity(book, HttpStatus.ACCEPTED);

    }

    @GetMapping(path = {"/getDistinctIsbn","search"},produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDtoQuery>>getDistinctIsbn(){
        List<BookDtoQuery> books=bookServiceImpl.fetchAllDistinctIsbn();
        return new ResponseEntity<>(books,HttpStatus.OK);
    }


    @GetMapping(path = "/seller/books",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getSellerBooks(HttpServletRequest request, @RequestHeader(name = "Token") String token){
        try {
            List<Book> books = bookServiceImpl.getBooksOfSeller(request, token);
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/deleteListing",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> deleteListing(@RequestBody Book book , HttpServletRequest request, @RequestHeader(name = "Token") String token){
        try{
            Book deletedBook = bookServiceImpl.deleteListing(book,request,token);
            return new ResponseEntity(deletedBook, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/isbn/{isbn}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> searchIsbn(@PathVariable("isbn") String isbn){
        List<Book> books=bookServiceImpl.getBooksByIsbn(isbn);
        return new ResponseEntity<>(books,HttpStatus.OK);
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
        List<Book> books = bookServiceImpl.filterByCategory(category);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping(path = "/search/{keyword}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> searchByKeyword(@PathVariable(value = "keyword") String keyword){
        List<Book> books = bookServiceImpl.searchByAny(keyword);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

}
