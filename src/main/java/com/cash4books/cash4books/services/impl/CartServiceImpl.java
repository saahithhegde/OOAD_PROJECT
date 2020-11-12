package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.controller.BookDetailsController;
import com.cash4books.cash4books.dto.cart.CartDTO;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Cart;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.exception.UserNotLoggedInException;
import com.cash4books.cash4books.repository.CartRepository;
import com.cash4books.cash4books.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    SessionServiceImpl sessionService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    BookServiceImpl bookService;

    @Autowired
    UserServiceImpl userService;

    Logger logger = LoggerFactory.getLogger(BookDetailsController.class);

    @Override
    public CartDTO addToCart(Book book, HttpServletRequest request,String token) throws UnsupportedEncodingException, Exception {
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            Users user=userService.getUserProfile(request,token);
            Book userOwnBook=bookService.getUserBook(book.getBookID(),user);
            if(userOwnBook!=null){
                throw new Exception("Cannot Purchase Your Own Product");
            }
            Cart cart = cartRepository.findCartByBookIDAndUsers(book.getBookID(),user);
            if(cart!=null){
                throw new Exception("Already Added To Cart");
            }
            Cart newCartItem=new Cart();
            newCartItem.setUser(user);
            newCartItem.setBookID(book.getBookID());
            cartRepository.save(newCartItem);
            logger.info("Successfully added book to cart:"+book.getTitle());
            CartDTO newCartItemWithBook=new CartDTO();
            Book bookDetails=bookService.getBookById(book.getBookID());
            newCartItemWithBook.setEmail(email);
            newCartItemWithBook.setBookID(bookDetails.getBookID());
            newCartItemWithBook.setBookDetails(bookDetails);
            return newCartItemWithBook;
        }
        else{
            logger.error("Failed to add book to cart, user is not logged in");
            throw new UserNotLoggedInException();
        }
    }

    @Override
    public CartDTO deleteFromCart(Cart cart, HttpServletRequest request,String token) throws UnsupportedEncodingException, Exception {
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            cartRepository.delete(cart);
            logger.info("Successfully deleted book from cart:");
            CartDTO deletedCartItem=new CartDTO();
            deletedCartItem.setEmail(email);
            deletedCartItem.setBookID(cart.getBookID());
            return deletedCartItem;
        }
        else{
            logger.error("Failed to delete book from cart, user is not logged in");
            throw new UserNotLoggedInException();
        }
    }

    @Override
    public List<CartDTO> getUserCart(HttpServletRequest request, String token) throws UnsupportedEncodingException, Exception {
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            Users user=userService.getUserProfile(request,token);
            List<Cart> cartItems = cartRepository.findAllByUsers(user);
            List<CartDTO> cartItemsWithBookDetails=getUserBookDetailsFromCart(cartItems);
            return cartItemsWithBookDetails;
        }
        else{
            throw new UserNotLoggedInException();
        }
    }

    private List<CartDTO> getUserBookDetailsFromCart(List<Cart> cartItems)throws Exception {
        List<CartDTO> cartItemsWithBookDetails=new ArrayList<CartDTO>();
        for (Cart items: cartItems) {
                CartDTO cartDetails=new CartDTO();
                Book bookDetails=new Book();
                bookDetails=bookService.getBookById(items.getBookID());
                cartDetails.setEmail(items.getUser().getEmail());
                cartDetails.setBookID(items.getBookID());
                cartDetails.setBookDetails(bookDetails);
                cartItemsWithBookDetails.add(cartDetails);
            }
            return cartItemsWithBookDetails;
    }

}
