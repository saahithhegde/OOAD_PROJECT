package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.controller.BookDetailsController;
import com.cash4books.cash4books.dto.cart.CartDto;
import com.cash4books.cash4books.dto.cart.UserCartDto;
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
    public CartDto addToCart(Book book, HttpServletRequest request, String token) throws UnsupportedEncodingException, Exception {
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            Users user=userService.getUserProfile(request,token);
            Book userOwnBook=bookService.getUserBook(book.getBookID(),user);
            if(userOwnBook!=null){
                logger.error("Cannot Purchase Your Own Product");
                throw new Exception("Cannot Purchase Your Own Product");
            }
            Cart cart = cartRepository.findCartByBookIDAndUsers(book.getBookID(),user);
            if(cart!=null){
                logger.error("Already Added To Cart");
                throw new Exception("Already Added To Cart");
            }
            Cart newCartItem=new Cart();
            newCartItem.setUser(user);
            newCartItem.setBookID(book.getBookID());
            cartRepository.save(newCartItem);
            logger.info("Successfully added book to cart:"+book.getTitle());
            CartDto newCartItemWithBook=new CartDto();
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
    public CartDto deleteFromCart(Book book, HttpServletRequest request, String token) throws UnsupportedEncodingException, Exception {
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            Users user=userService.getUserProfile(request,token);
            Cart cart=cartRepository.findCartByBookIDAndUsers(book.getBookID(), user);
            if(cart==null){
                logger.error("Error Please Contact Administrator");
                throw new Exception("Error Please Contact Administrator");
            }
            cartRepository.delete(cart);
            logger.info("Successfully deleted book from cart:");
            CartDto deletedCartItem=new CartDto();
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
    public UserCartDto getUserCart(HttpServletRequest request, String token) throws UnsupportedEncodingException, Exception {
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            Users user=userService.getUserProfile(request,token);
            List<Cart> cartItems = cartRepository.findAllByUsers(user);
            UserCartDto cartItemsWithBookDetails=getUserBookDetailsFromCart(cartItems);
            return cartItemsWithBookDetails;
        }
        else{
            logger.error("Failed to get cart details, user is not logged in");
            throw new UserNotLoggedInException();
        }
    }

    private UserCartDto getUserBookDetailsFromCart(List<Cart> cartItems)throws Exception {
        UserCartDto userCart=new UserCartDto();
        int total=0;
        List<CartDto> cartItemsWithBookDetails=new ArrayList<CartDto>();
        for (Cart items: cartItems) {
                CartDto cartDetails=new CartDto();
                Book bookDetails=new Book();
                bookDetails=bookService.getBookById(items.getBookID());
                if(bookDetails!=null) {
                    cartDetails.setEmail(items.getUser().getEmail());
                    cartDetails.setBookID(items.getBookID());
                    cartDetails.setBookDetails(bookDetails);
                    cartItemsWithBookDetails.add(cartDetails);
                    total += bookDetails.getPrice();
                }
                else{
                    cartRepository.delete(items);
            }
        }
        userCart.setTotal(total);
        userCart.setCartDetails(cartItemsWithBookDetails);
        return userCart;
    }
}
