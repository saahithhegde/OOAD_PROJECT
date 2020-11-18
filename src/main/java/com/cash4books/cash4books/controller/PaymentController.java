package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.cart.CartDto;
import com.cash4books.cash4books.dto.cart.UserCartDto;
import com.cash4books.cash4books.dto.orders.BooksOrderDto;
import com.cash4books.cash4books.entity.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@Transactional
public class PaymentController {


    @Autowired
    PaymentServiceImpl paymentServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @GetMapping(value="/submit" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooksOrderDto> submitPayment(HttpServletRequest request, @RequestHeader(name = "Token") String token, @RequestHeader(name = "paymentType") String paymentType){
        BooksOrderDto booksOrderDto = null;
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
            return new ResponseEntity(paymentServiceImpl.getBooksJson(availableBooks),HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Orders orders;
        orders = paymentServiceImpl.createOrder(availableBooks,buyer.getEmail(),paymentType);

        try {
            orders = paymentServiceImpl.executeTransaction(orders,availableBooks,buyer);
            List<OrderDetails> orderDetailsList = paymentServiceImpl.saveOrderDetails(availableBooks, orders.getOrderID(), buyer.getEmail());
            booksOrderDto = new BooksOrderDto();
            booksOrderDto.setOrders(orders);
            booksOrderDto.setOrderDetails(orderDetailsList);
        } catch (Exception e) {
            return new ResponseEntity("Failed to execute transaction, Please try again later",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(booksOrderDto,HttpStatus.ACCEPTED);
    }


}
