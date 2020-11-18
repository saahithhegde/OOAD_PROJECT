package com.cash4books.cash4books.services;

import com.cash4books.cash4books.dto.payment.PaymentDto;
import com.cash4books.cash4books.entity.UserPaymentTypes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface PaymentService {
    public PaymentDto confirmPayment(HttpServletRequest request, String token)throws UnsupportedEncodingException, Exception;
    public List<UserPaymentTypes> getUserPayments(HttpServletRequest request,String token) throws Exception;
}
