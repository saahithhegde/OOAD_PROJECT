package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.orders.BooksOrderDto;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.OrderDetails;
import com.cash4books.cash4books.entity.Orders;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.services.impl.PaymentServiceImpl;
import com.cash4books.cash4books.services.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PaymentController.class)
@WithMockUser
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PaymentServiceImpl paymentService;

    @MockBean
    UserServiceImpl userService;

    BooksOrderDto booksOrderDto;
    Users buyer;
    List<Integer> bookIdList;
    List<Book> availableBooks;
    Users user1;
    Orders orders;
    OrderDetails orderDetails;
    List<OrderDetails> orderDetailsList;

    @Before
    public void init() {
        booksOrderDto = new BooksOrderDto();
        buyer = new Users();
        buyer.setEmail("sample2@utdallas.edu");
        bookIdList = new ArrayList<>();
        availableBooks = new ArrayList<>();
        user1 = new Users();
        user1.setEmail("u1");
        Book book = new Book();
        book.setBookID(1);
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        book.setUsers(user1);
        bookIdList.add(1);
        availableBooks.add(book);
        long d = System.currentTimeMillis();
        Date date = new Date(d);
        orders = new Orders();
        orders.setOrderID(1);
        orders.setPaymentType("credit card");
        orders.setTotal(10.0);
        orders.setOrderDate(date);
        orderDetails = new OrderDetails();
        orderDetails.setOrderID(1);
        orderDetails.setBookID(1);
        orderDetailsList = new ArrayList<>();
        orderDetailsList.add(orderDetails);
        booksOrderDto.setOrders(orders);
        booksOrderDto.setOrderDetails(orderDetailsList);

    }

    @Test
    public void submitPaymentTest() throws Exception {
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(buyer);
        when(paymentService.getBooksInCart(Mockito.any(Users.class))).thenReturn(bookIdList);
        when(paymentService.getAvailableBooks(Mockito.anyList())).thenReturn(availableBooks);
        when(paymentService.createOrder(Mockito.anyList(),Mockito.anyString(),Mockito.anyString())).thenReturn(orders);
        when(paymentService.executeTransaction(Mockito.any(Orders.class),Mockito.anyList(),Mockito.any(Users.class))).thenReturn(orders);
        when(paymentService.saveOrderDetails(Mockito.anyList(),Mockito.anyInt(),Mockito.anyString())).thenReturn(orderDetailsList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payment/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","test")
                .header("paymentType","credit card")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.orders.orderID").exists())
                .andExpect(jsonPath("$.orders.orderID").value(1))
                .andExpect(jsonPath("$.orderDetails.[0].orderID").exists())
                .andExpect(jsonPath("$.orderDetails.[0].orderID").value(1));

    }

    @Test
    public void submitPaymentUserNotLoggedInTest() throws Exception {
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenThrow(Exception.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payment/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","test")
                .header("paymentType","credit card")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void submitPaymentWithEmptyCartTest() throws Exception {
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(buyer);
        when(paymentService.getBooksInCart(Mockito.any(Users.class))).thenThrow(Exception.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payment/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","test")
                .header("paymentType","credit card")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void submitPaymentBooksNotAvailableTest() throws Exception {
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(buyer);
        when(paymentService.getBooksInCart(Mockito.any(Users.class))).thenReturn(bookIdList);
        when(paymentService.getAvailableBooks(Mockito.anyList())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payment/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","test")
                .header("paymentType","credit card")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }


    @Test
    public void submitPaymentTransactionFailureTest() throws Exception {
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(buyer);
        when(paymentService.getBooksInCart(Mockito.any(Users.class))).thenReturn(bookIdList);
        when(paymentService.getAvailableBooks(Mockito.anyList())).thenReturn(availableBooks);
        when(paymentService.createOrder(Mockito.anyList(),Mockito.anyString(),Mockito.anyString())).thenReturn(orders);
        when(paymentService.executeTransaction(Mockito.any(Orders.class),Mockito.anyList(),Mockito.any(Users.class))).thenThrow(Exception.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payment/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","test")
                .header("paymentType","credit card")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }


}
