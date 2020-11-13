package com.cash4books.cash4books.services;

import com.cash4books.cash4books.dto.payment.PaymentDto;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface PaymentService {
    public PaymentDto confirmPayment(HttpServletRequest request, String token)throws UnsupportedEncodingException, Exception;
}
