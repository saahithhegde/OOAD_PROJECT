package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.controller.PaymentController;
import com.cash4books.cash4books.entity.*;
import com.cash4books.cash4books.exception.UserNotLoggedInException;
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

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    UserPayementRepository userPayementRepository;

    @Autowired
    UserServiceImpl userService;

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
          logger.error("User Cart is empty!");
          throw new Exception("User Cart is empty!");
      }
  }

  public List<Book> getAvailableBooks(List<Integer> orderedBookList){
      List<Book> availableBooks = bookRepository.findByBookIDIn(orderedBookList);
      return availableBooks;
  }

  public Orders createOrder(List<Book> orderedBooks, String buyer, String paymentType) throws JsonProcessingException {
      Orders orders = new Orders();
      ObjectMapper objectMapper = JsonUtil.getObjectMapper();
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
        else {
            logger.error("Failed to execute transaction");
            throw new Exception("Failed to execute transaction");
        }
        return orders;
    }

    public List<UserPaymentTypes> getUserPayments(HttpServletRequest request,String token) throws Exception{
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            Users user=userService.getUserProfile(request,token);
            List<UserPaymentTypes> userPaymentTypes=userPayementRepository.findAllByUsers(user);
            return userPaymentTypes;
        }
        else{
            logger.error("Failed to get user payment details, User not logged in");
            throw new UserNotLoggedInException();
        }
    }

    public UserPaymentTypes addUserPaymentsType(HttpServletRequest request,String token,UserPaymentTypes userPaymentTypes)throws Exception{
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            Users user=userService.getUserProfile(request,token);
            userPaymentTypes.setUsers(user);
            UserPaymentTypes userPaymentTypes1=userPayementRepository.findUserPaymentTypesByCardNumber(userPaymentTypes.getCardNumber());
            if(userPaymentTypes1==null){
                userPayementRepository.save(userPaymentTypes);
                logger.info("Successfully Added new User Payment Detail");

            }
            else {
                logger.error("Failed to add new card, Card Number Already Present");
                throw new Exception("Card Number Already Present");
            }
            return userPaymentTypes;
        }
        else{
            logger.error("Failed to add new card, User not logged in");
            throw new UserNotLoggedInException();
        }
    }

    public UserPaymentTypes deleteUserPaymentsType(HttpServletRequest request,String token,UserPaymentTypes userPaymentTypes)throws Exception{
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            Users user=userService.getUserProfile(request,token);
            userPaymentTypes.setUsers(user);
            userPayementRepository.delete(userPaymentTypes);
            logger.info("Successfully deleted User Payment Detail");
            return userPaymentTypes;
        }
        else{
            logger.error("Failed to delete user payment details, User not logged in");
            throw new UserNotLoggedInException();
        }
    }


}
