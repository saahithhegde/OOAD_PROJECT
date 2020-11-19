package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Orders;
import com.cash4books.cash4books.entity.UserPaymentTypes;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WithMockUser
public class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    CartRepository cartRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    OrderRepository orderRepository;

    @Mock
    SessionServiceImpl sessionService;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    UserServiceImpl userService;

    @Mock
    UserPayementRepository userPayementRepository;


    @Mock
    OrderDetailsRepository orderDetailsRepository;

    List<Book> booksList;
    Book book;
    List<Integer> bookIdList;
    Orders orders;
    Users users;
    UserPaymentTypes userPaymentTypes;
    List<UserPaymentTypes> userPaymentTypesList;
    String email;
    @Before
    public void init(){
        email = "test";
        book = new Book();
        book.setBookID(1);
        book.setIsbn("1234");
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        book.setEmail(email);
        booksList = new ArrayList<>();
        booksList.add(book);
        bookIdList = new ArrayList<>();
        bookIdList.add(1);
        long d = System.currentTimeMillis();
        Date date = new Date(d);
        orders = new Orders();
        orders.setOrderID(1);
        orders.setPaymentType("credit card");
        orders.setTotal(10.0);
        orders.setOrderDate(date);
        users = new Users();
        users.setEmail("test");
        userPaymentTypes = new UserPaymentTypes();
        userPaymentTypes.setUsers(users);
        userPaymentTypes.setCardName("xyz");
        userPaymentTypes.setCardNumber(123456789L);
        userPaymentTypes.setPaymentType("Credit card");
        userPaymentTypes.setPaymentTypeID(1);
        userPaymentTypesList = new ArrayList<>();
        userPaymentTypesList.add(userPaymentTypes);
    }
    @Test
    public void createOrderTest() throws JsonProcessingException {
        Orders orders = paymentService.createOrder(booksList,"test","credit card");
        Assert.assertEquals("test",orders.getBuyerID());
        Assert.assertEquals("credit card",orders.getPaymentType());
    }

    @Test
    public void getAvailableBookTest(){
        when(bookRepository.findByBookIDIn(Mockito.anyList())).thenReturn(booksList);
        Assert.assertEquals(1,paymentService.getAvailableBooks(bookIdList).get(0).getBookID(),0);
    }

    @Test
    public void executeTransactionTest() throws Exception {
        when(orderRepository.save(Mockito.any(Orders.class))).thenReturn(orders);
        doNothing().when(bookRepository).deleteAll(Mockito.anyIterable());
        doNothing().when(cartRepository).deleteCartByUsers(Mockito.any(Users.class));
        Assert.assertEquals(1,paymentService.executeTransaction(orders,booksList,users).getOrderID(),0);

    }

    @Test
    public void getUserPaymentTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(users);
        when(userPayementRepository.findAllByUsers(Mockito.any(Users.class))).thenReturn(userPaymentTypesList);
        List<UserPaymentTypes> result = paymentService.getUserPayments(httpServletRequest, "test");
        Assert.assertEquals("Credit card",result.get(0).getPaymentType());
    }

    @Test
    public void getUserPaymentUserNotLoggedTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            paymentService.getUserPayments(httpServletRequest,"test");
        });
        String expectedMessage = "User Not Logged In";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void addUserPaymentsTypeTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(users);
        when(userPayementRepository.findUserPaymentTypesByCardNumber(Mockito.anyLong())).thenReturn(null);
        when(userPayementRepository.save(Mockito.any(UserPaymentTypes.class))).thenReturn(userPaymentTypes);
        UserPaymentTypes result = paymentService.addUserPaymentsType(httpServletRequest, "test", userPaymentTypes);
        Assert.assertEquals("test",result.getUsers().getEmail());
    }

    @Test
    public void addUserPaymentsTypeNumberExistsTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(users);
        when(userPayementRepository.findUserPaymentTypesByCardNumber(Mockito.anyLong())).thenReturn(userPaymentTypes);
        when(userPayementRepository.save(Mockito.any(UserPaymentTypes.class))).thenReturn(userPaymentTypes);
        Exception exception = assertThrows(Exception.class, () -> {
            paymentService.addUserPaymentsType(httpServletRequest,"test",userPaymentTypes);
        });
        String expectedMessage = "Card Number Already Present";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void deleteUserPaymentsTypeTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(users);
        when(userPayementRepository.findUserPaymentTypesByCardNumber(Mockito.anyLong())).thenReturn(null);
        doNothing().when(userPayementRepository).delete(Mockito.any(UserPaymentTypes.class));
        UserPaymentTypes result = paymentService.deleteUserPaymentsType(httpServletRequest, "test", userPaymentTypes);
        Assert.assertEquals("test",result.getUsers().getEmail());
    }

    @Test
    public void deleteUserPaymentsTypeUserNotLoggedTest() throws Exception {
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            paymentService.deleteUserPaymentsType(httpServletRequest,"test",userPaymentTypes);
        });
        String expectedMessage = "User Not Logged In";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }
}
