package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.controller.PaymentController;
import com.cash4books.cash4books.dto.orders.BooksOrderDto;
import com.cash4books.cash4books.entity.Book;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

  public Orders createOrder(List<Book> orderedBooks, Users buyer, String paymentType) throws JsonProcessingException {
      Orders orders = new Orders();
      ObjectMapper objectMapper = JsonUtil.getObjectMapper();
      orders.setUsers(buyer);
      long d = System.currentTimeMillis();
      Date date = new Date(d);
      orders.setOrderDate(date);
      List<BooksOrderDto> bookList = createOderedBooksDTO(orderedBooks);
      double totalPrice = 0.0;
      for(BooksOrderDto booksOrderDto : bookList){
          totalPrice += booksOrderDto.getPrice();
      }
      orders.setTotal(totalPrice);
      orders.setPaymentType(paymentType);
      String booksJson = objectMapper.writeValueAsString(bookList);
      orders.setBooksBought(booksJson);
      orders = orderRepository.save(orders);
      return orders;
  }

  public List<BooksOrderDto> createOderedBooksDTO(List<Book> orderedBooks){
      List<BooksOrderDto> bookList = orderedBooks.stream()
              .map(this::convertToDto)
              .collect(Collectors.toList());
      return bookList;

  }

  public BooksOrderDto convertToDto(Book book) {
        BooksOrderDto booksOrderDto = modelMapper.map(book, BooksOrderDto.class);
        booksOrderDto.setSellerID(book.getUsers().getEmail());
        return booksOrderDto;
    }

    public String getBooksJson(List<Book> books)  {
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        try {
            return objectMapper.writeValueAsString(books);
        } catch (JsonProcessingException e) {
            return "";
        }
    }


}
