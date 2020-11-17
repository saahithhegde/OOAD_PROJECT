package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.controller.PaymentController;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.OrderDetails;
import com.cash4books.cash4books.entity.Orders;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.*;
import com.cash4books.cash4books.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional(rollbackFor = Exception.class)
public class PaymentServiceImpl {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    SessionServiceImpl sessionService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    Logger logger = LoggerFactory.getLogger(PaymentController.class);

  public List<Integer> getBooksInCart(Users user) throws Exception {
      List<BooksInCart> booksInCarts = cartRepository.getBookIDByUsers(user);
      if(booksInCarts!=null && booksInCarts.size()>0){
          List<Integer> bookIDList = new ArrayList<>();
          for(BooksInCart books: booksInCarts){
              bookIDList.add(books.getBookID());
          }
          return bookIDList;
      } else {
          throw new Exception("Cart is empty");
      }
  }

  public List<Book> getAvailableBooks(List<Integer> orderedBookList){
      List<Book> availableBooks = bookRepository.findByBookIDIn(orderedBookList);
      return availableBooks;
  }

  public Orders createOrder(List<Book> orderedBooks, String buyer, String paymentType) {
      Orders orders = new Orders();
      orders.setBuyerID(buyer);
      long d = System.currentTimeMillis();
      Date date = new Date(d);
      orders.setOrderDate(date);

      double totalPrice = 0.0;
      for(Book b : orderedBooks){
          totalPrice += b.getPrice();
      }
      orders.setBuyerID(buyer);
      orders.setTotal(totalPrice);
      orders.setPaymentType(paymentType);
      return orders;
  }

  public List<OrderDetails> saveOrderDetails(List<Book> orderedBooks, Integer orderID, String buyer){
      List<OrderDetails> bookList = createOrderDetails(orderedBooks,orderID, buyer);
      Iterable<OrderDetails> temp = orderDetailsRepository.saveAll(bookList);
      return StreamSupport.stream(temp.spliterator(), false).collect(Collectors.toList());
  }

  public List<OrderDetails> createOrderDetails(List<Book> orderedBooks, Integer orderID, String buyer){
      List<OrderDetails> bookList = orderedBooks.stream()
              .map(book -> convertToOrderDetails(book,orderID,buyer))
              .collect(Collectors.toList());
      return bookList;

  }

  public OrderDetails convertToOrderDetails(Book book, Integer orderID, String buyer) {
        OrderDetails orderDetails = modelMapper.map(book, OrderDetails.class);
        orderDetails.setOrderID(orderID);
        orderDetails.setBuyer(buyer);
        orderDetails.setSeller(book.getEmail());
        return orderDetails;
    }

    public String getBooksJson(List<Book> books)  {
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        try {
            return objectMapper.writeValueAsString(books);
        } catch (JsonProcessingException e) {
            return "";
        }
    }


    public Orders executeTransaction(Orders orders, List<Book> books, Users buyer) throws Exception {
        orders = orderRepository.save(orders);
        if(orders!=null){
        bookRepository.deleteAll(books);
        cartRepository.deleteCartByUsers(buyer);
        }
        else throw new Exception("Failed to execute transaction");
        return orders;
    }

}
