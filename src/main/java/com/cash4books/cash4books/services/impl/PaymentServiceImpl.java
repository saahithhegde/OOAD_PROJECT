package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.controller.PaymentController;
import com.cash4books.cash4books.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentServiceImpl {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    SessionServiceImpl sessionService;

    Logger logger = LoggerFactory.getLogger(PaymentController.class);



}
