package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Orders;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.services.impl.CartServiceImpl;
import com.cash4books.cash4books.services.impl.PaymentServiceImpl;
import com.cash4books.cash4books.services.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    CartServiceImpl cartServiceImpl;

    @Autowired
    PaymentServiceImpl paymentServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @GetMapping(value="/submit" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Orders> submitPayment(HttpServletRequest request, @RequestHeader(name = "Token") String token, String paymentType){
        //TODO: Handle transaction, and delete books from the available books after buy
        Users buyer;
        try {
             buyer = userServiceImpl.getUserProfile(request,token);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        List<Integer> bookIdList;
        try{
            bookIdList = paymentServiceImpl.getBooksInCart(buyer);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        List<Book> availableBooks;
        availableBooks = paymentServiceImpl.getAvailableBooks(bookIdList);
        if(availableBooks==null || availableBooks.size() != bookIdList.size()){
            return new ResponseEntity("Few books in cart are not available. Available books are:"+paymentServiceImpl.getBooksJson(availableBooks),HttpStatus.BAD_REQUEST);
        }
        Orders orders;
        try {
            orders = paymentServiceImpl.createOrder(availableBooks,buyer,paymentType);
        } catch (JsonProcessingException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            orders = paymentServiceImpl.executeTransaction(orders,availableBooks,buyer);
        } catch (Exception e) {
            return new ResponseEntity("Failed to execute transaction, Please try again later",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(orders,HttpStatus.ACCEPTED);
    }

}
