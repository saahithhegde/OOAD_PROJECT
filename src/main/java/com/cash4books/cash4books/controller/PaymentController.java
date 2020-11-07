package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.cart.CartDTO;
import com.cash4books.cash4books.services.impl.CartServiceImpl;
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

    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @GetMapping(value="/submit" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CartDTO>> submitPayment(HttpServletRequest request, @RequestHeader(name = "Token") String token){
        try {
            return
        } catch (Exception e) {

        }
    }

}
