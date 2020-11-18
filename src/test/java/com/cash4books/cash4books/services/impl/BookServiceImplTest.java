package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.book.BookDtoQuery;
import com.cash4books.cash4books.dto.orders.BooksOrderDto;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.OrderDetails;
import com.cash4books.cash4books.entity.Orders;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.exception.UserNotLoggedInException;
import com.cash4books.cash4books.repository.*;
import com.cash4books.cash4books.services.SessionService;
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

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WithMockUser
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    SessionServiceImpl sessionServiceImpl;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderDetailsRepository orderDetailsRepository;

    @Mock
    SessionService sessionService;



    Book expectedBook;
    Users users;
    String email;
    List<Book> list;
    List<BookDtoQuery> bookDtoQueryList;
    BookDtoQuery bookDtoQuery;
    List<Integer> orderIDList;
    Orders orders;
    OrderDetails orderDetails;
    List<OrderDetails> orderDetailsList;
    BooksOrderDto booksOrderDto;
    List<Orders> ordersList;

    @Before
    public void init(){
        booksOrderDto = new BooksOrderDto();
        expectedBook = new Book();
        expectedBook.setBookID(1);
        expectedBook.setIsbn("1234");
        expectedBook.setTitle("book_5");
        expectedBook.setAuthor("abc");
        expectedBook.setCategory("SE");
        expectedBook.setPrice(2.5);
        users = new Users();
        email = "test";
        list = new ArrayList<>();
        list.add(expectedBook);
        bookDtoQuery = new BookDtoQuery("1234","book_5","abc","SE","description",null,1l);
        bookDtoQueryList = new ArrayList<>();
        bookDtoQueryList.add(bookDtoQuery);
        orderIDList = new ArrayList<>();
        orderIDList.add(1);
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
        ordersList = new ArrayList<>();
        ordersList.add(orders);
    }

    @Test
    public void shouldReturnBook() throws Exception {

        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(users);
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        Book actualBook = bookService.addBook(expectedBook, httpServletRequest,  "test");
        assertEquals(expectedBook.getTitle(),actualBook.getTitle());
        assertEquals(expectedBook.getAuthor(),actualBook.getAuthor());
        assertEquals(expectedBook.getCategory(),actualBook.getCategory());
        assertEquals(expectedBook.getPrice(),actualBook.getPrice(),0);
    }

    @Test(expected = Exception.class)
    public void shouldThrowException() throws Exception {
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(users);
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        Book actualBook = bookService.addBook(expectedBook,httpServletRequest, "");

    }

    @Test
    public void filterBookByAuthorTest() throws Exception {
        Book expectedBook = new Book();
        expectedBook.setTitle("book_5");
        expectedBook.setAuthor("abc");
        expectedBook.setCategory("SE");
        expectedBook.setPrice(2.5);
        List<Book> list = new ArrayList<>();
        list.add(expectedBook);
        when(bookRepository.findAllByAuthor(eq("abc"))).thenReturn(list);
        List<Book> result = bookService.filterByAuthor( "abc");
        assertEquals(1,result.size());
    }

    @Test
    public void filterBookByTitleTest() throws Exception {
        when(bookRepository.findByTitleContaining(eq("book_5"))).thenReturn(list);
        List<Book> result = bookService.getBooksWithTitle( "book_5");
        assertEquals(1,result.size());
    }

    @Test
    public void filterBookByCategoryTest() throws Exception {
        when(bookRepository.findAllByCategory(eq("SE"))).thenReturn(list);
        List<Book> result = bookService.filterByCategory( "SE");
        assertEquals(1,result.size());
    }

    @Test
    public void filterBookBySellerTest() throws Exception {
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(users);
        when(bookRepository.findAllByEmail(Mockito.anyString())).thenReturn(list);
        List<Book> result = bookService.getBooksBySeller( "test");
        assertEquals(1,result.size());
    }

    @Test
    public void getBooksByIsbnTest(){
        when(bookRepository.findAllByIsbn(Mockito.anyString())).thenReturn(list);
        Assert.assertEquals(1,bookService.getBooksByIsbn("1234").size());
    }

    @Test
    public void fetchAllDistinctIsbnTest(){
        when(bookRepository.fetchAllDisticntIsbn()).thenReturn(bookDtoQueryList);
        Assert.assertEquals(1,bookService.fetchAllDistinctIsbn().size());
    }

    @Test
    public void getBookByIDTest(){
        when(bookRepository.findByBookID(Mockito.anyInt())).thenReturn(expectedBook);
        Assert.assertEquals(expectedBook.getBookID(),bookService.getBookById(1).getBookID());
    }

    @Test
    public void getSellHistoryTest() throws UnsupportedEncodingException, UserNotLoggedInException {
        System.out.println(email);
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(orderDetailsRepository.findAllOrdersOfSeller(Mockito.anyString())).thenReturn(orderIDList);
        when(orderDetailsRepository.findAllByOrderID(Mockito.anyInt())).thenReturn(orderDetailsList);
        when(orderRepository.findByOrderID(Mockito.anyInt())).thenReturn(orders);
        List<BooksOrderDto> sellerHistory = bookService.getSellHistory(httpServletRequest, "test");
        Assert.assertEquals(1,sellerHistory.get(0).getOrders().getOrderID(),0);
        Assert.assertEquals(1,sellerHistory.get(0).getOrderDetails().size());
    }

    @Test
    public void getBuyHistoryTest() throws UnsupportedEncodingException, UserNotLoggedInException {
        System.out.println(email);
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(orderRepository.findAllByBuyerID(Mockito.anyString())).thenReturn(ordersList);
        when(orderDetailsRepository.findAllByOrderID(Mockito.anyInt())).thenReturn(orderDetailsList);
        when(orderRepository.findByOrderID(Mockito.anyInt())).thenReturn(orders);
        List<BooksOrderDto> buyHistory = bookService.getBuyHistory(httpServletRequest, "test");
        Assert.assertEquals(1,buyHistory.get(0).getOrders().getOrderID(),0);
        Assert.assertEquals(1,buyHistory.get(0).getOrderDetails().size());
    }

    @Test
    public void getBooksOfSellerTest() throws Exception {
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(users);
        when(bookRepository.findAllByUsers(Mockito.any(Users.class))).thenReturn(list);
        List<Book> sellerBooks = bookService.getBooksOfSeller(httpServletRequest, "test");
        Assert.assertEquals(list.get(0).getBookID(),sellerBooks.get(0).getBookID());
    }

    @Test
    public void searchByAnyTest(){
        when(bookRepository.search(Mockito.anyString())).thenReturn(list);
        List<Book> result = bookService.searchByAny("abc");
        Assert.assertEquals(1,result.size());
        Assert.assertEquals("abc",result.get(0).getAuthor());
    }

@Test
    public void deleteListingTest() throws Exception {
    when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
    when(bookRepository.findByBookID(Mockito.anyInt())).thenReturn(expectedBook);
    doNothing().when(bookRepository).delete(Mockito.any(Book.class));
    Book result = bookService.deleteListing(expectedBook, httpServletRequest, "test");
    Assert.assertEquals(expectedBook.getBookID(),result.getBookID());
}

    @Test
    public void deleteListingUserNotLoggedTest() throws Exception {
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            bookService.deleteListing(expectedBook,httpServletRequest,"test");
        });
        String expectedMessage = "User Not Logged In";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }


}
