package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.cart.CartDto;
import com.cash4books.cash4books.dto.cart.UserCartDto;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.services.impl.CartServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    Logger logger = LoggerFactory.getLogger(BookDetailsController.class);

    @Autowired
    CartServiceImpl cartServiceImpl;


    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartDto> addBookToCart(@RequestBody Book book , HttpServletRequest request, @RequestHeader(name = "Token") String token){
        try {
            CartDto newCartItem = cartServiceImpl.addToCart(book,request,token);
            return new ResponseEntity(newCartItem, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
      }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartDto> DeleteBookFromCart(@RequestBody Book book , HttpServletRequest request, @RequestHeader(name = "Token") String token){
        try {
            CartDto deletedCartItem = cartServiceImpl.deleteFromCart(book,request,token);
            return new ResponseEntity(deletedCartItem, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value="/getUserCart" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserCartDto> getUserCart(HttpServletRequest request, @RequestHeader(name = "Token") String token){
        try {
            UserCartDto userCartItem = cartServiceImpl.getUserCart(request,token);
            return new ResponseEntity(userCartItem, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

}
