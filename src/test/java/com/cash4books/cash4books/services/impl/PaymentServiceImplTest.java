package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Orders;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.*;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    OrderDetailsRepository orderDetailsRepository;

    List<Book> booksList;
    Book book;
    List<Integer> bookIdList;
    Orders orders;
    Users users;
    @Before
    public void init(){
        book = new Book();
        book.setBookID(1);
        book.setIsbn("1234");
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
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
    }
    @Test
    public void createOrderTest(){
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
}
