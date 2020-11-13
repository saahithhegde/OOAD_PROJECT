package com.cash4books.cash4books.services;

import com.cash4books.cash4books.dto.cart.CartDTO;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Cart;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface CartService {
    public CartDTO addToCart(Book book, HttpServletRequest request, String token) throws UnsupportedEncodingException, Exception;
    public CartDTO deleteFromCart(Book book,HttpServletRequest request,String token) throws UnsupportedEncodingException, Exception;
    public List<CartDTO> getUserCart(HttpServletRequest request,String token)throws UnsupportedEncodingException, Exception;
}
