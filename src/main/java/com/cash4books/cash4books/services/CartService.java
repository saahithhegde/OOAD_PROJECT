package com.cash4books.cash4books.services;

import com.cash4books.cash4books.dto.cart.CartDto;
import com.cash4books.cash4books.dto.cart.UserCartDto;
import com.cash4books.cash4books.entity.Book;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface CartService {
    public CartDto addToCart(Book book, HttpServletRequest request, String token) throws UnsupportedEncodingException, Exception;
    public CartDto deleteFromCart(Book book, HttpServletRequest request, String token) throws UnsupportedEncodingException, Exception;
    public UserCartDto getUserCart(HttpServletRequest request, String token)throws UnsupportedEncodingException, Exception;
}
