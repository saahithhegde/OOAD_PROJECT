package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.cart.CartDTO;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Cart;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.CartRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WithMockUser
public class CartServiceImplTest {
    @InjectMocks
    CartServiceImpl cartService;

    @Mock
    SessionServiceImpl sessionService;

    @Mock
    UserServiceImpl userService;

    @Mock
    BookServiceImpl bookService;

    @Mock
    CartRepository cartRepository;

    @Mock
    HttpServletRequest httpServletRequest;

    String email;
    Users user;
    Book book;
    Cart cart;
    List<Cart> cartList;
    List<CartDTO> cartDTOList;
    CartDTO cartDTO;

    @Before
    public void init(){
        email = "test";
        user = new Users();
        user.setEmail("test");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        book = new Book();
        book.setBookID(1);
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        cart = new Cart();
        cart.setBookID(1);
        cart.setUser(user);
        cart.setCartID(1);
        cartList = new ArrayList<>();
        cartList.add(cart);
        cartDTO = new CartDTO();
        cartDTO.setEmail("test");
        cartDTO.setBookID(1);
        cartDTOList = new ArrayList<>();
        cartDTOList.add(cartDTO);

    }

    @Test
    public void addToCartTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user);
        when(bookService.getUserBook(Mockito.anyInt(),Mockito.any(Users.class))).thenReturn(null);
        when(cartRepository.findCartByBookIDAndUsers(Mockito.anyInt(),Mockito.any(Users.class))).thenReturn(null);
        when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cart);
        when(bookService.getBookById(Mockito.anyInt())).thenReturn(book);
        CartDTO newCart = cartService.addToCart(book,httpServletRequest,"test");
        Assert.assertEquals(1,newCart.getBookID(),0);
        Assert.assertEquals("test",newCart.getEmail());
    }

    @Test
    public void addToCartBuyOwnBookTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user);
        when(bookService.getUserBook(Mockito.anyInt(),Mockito.any(Users.class))).thenReturn(book);
        when(cartRepository.findCartByBookIDAndUsers(Mockito.anyInt(),Mockito.any(Users.class))).thenReturn(cart);
        when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cart);
        when(bookService.getBookById(Mockito.anyInt())).thenReturn(book);
        Exception exception = assertThrows(Exception.class, () -> {
            cartService.addToCart(book,httpServletRequest,"test");
        });

        String expectedMessage = "Cannot Purchase Your Own Product";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void addToCartDuplicateTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user);
        when(bookService.getUserBook(Mockito.anyInt(),Mockito.any(Users.class))).thenReturn(null);
        when(cartRepository.findCartByBookIDAndUsers(Mockito.anyInt(),Mockito.any(Users.class))).thenReturn(cart);
        when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cart);
        when(bookService.getBookById(Mockito.anyInt())).thenReturn(book);
        Exception exception = assertThrows(Exception.class, () -> {
            cartService.addToCart(book,httpServletRequest,"test");
        });

        String expectedMessage = "Already Added To Cart";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void addToCartUserNotLoggedTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            cartService.addToCart(book,httpServletRequest,"test");
        });

        String expectedMessage = "User Not Logged In";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void deleteFromCartTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user);
        when(cartRepository.findCartByBookIDAndUsers(Mockito.anyInt(),Mockito.any(Users.class))).thenReturn(cart);
        doNothing().when(cartRepository).delete(Mockito.any(Cart.class));
        when(bookService.getBookById(Mockito.anyInt())).thenReturn(book);
        CartDTO deletedCart = cartService.deleteFromCart(book,httpServletRequest,"test");
        Assert.assertEquals(1,deletedCart.getBookID(),0);
        Assert.assertEquals("test",deletedCart.getEmail());
    }

    @Test
    public void deleteFromCartUserNotLoggedTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            cartService.deleteFromCart(book,httpServletRequest,"test");
        });

        String expectedMessage = "User Not Logged In";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void deleteFromCartFailureTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user);
        when(cartRepository.findCartByBookIDAndUsers(Mockito.anyInt(),Mockito.any(Users.class))).thenReturn(null);
        doNothing().when(cartRepository).delete(Mockito.any(Cart.class));
        when(bookService.getBookById(Mockito.anyInt())).thenReturn(book);
        Exception exception = assertThrows(Exception.class, () -> {
            cartService.deleteFromCart(book,httpServletRequest,"test");
        });

        String expectedMessage = "Error Please Contact Administrator";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void getUserCartTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user);
        when(cartRepository.findAllByUsers(Mockito.any(Users.class))).thenReturn(cartList);
        when(bookService.getBookById(Mockito.anyInt())).thenReturn(book);
        List<CartDTO> result = cartService.getUserCart(httpServletRequest, "test");
        Assert.assertEquals(1,result.get(0).getBookID(),0);
    }


}
