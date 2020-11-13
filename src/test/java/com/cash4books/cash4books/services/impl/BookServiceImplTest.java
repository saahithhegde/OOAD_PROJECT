package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.BookRepository;
import com.cash4books.cash4books.repository.UserRepository;
import com.cash4books.cash4books.services.SessionService;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    SessionService sessionService;



    @Test
    public void shouldReturnBook() throws Exception {
        Book expectedBook = new Book();
        expectedBook.setTitle("book_5");
        expectedBook.setAuthor("abc");
        expectedBook.setCategory("SE");
        expectedBook.setPrice(2.5);
        Users users = new Users();
        String email = "test";
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
       // MockHttpServletRequest httpServletRequest = new MockHttpServletRequest( );
        Book expectedBook = new Book();
        expectedBook.setTitle("book_5");
        expectedBook.setAuthor("abc");
        expectedBook.setCategory("SE");
        expectedBook.setPrice(2.5);
        Users users = new Users();
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
        Book expectedBook = new Book();
        expectedBook.setTitle("book_5");
        expectedBook.setAuthor("abc");
        expectedBook.setCategory("SE");
        expectedBook.setPrice(2.5);
        List<Book> list = new ArrayList<>();
        list.add(expectedBook);
        when(bookRepository.findByTitleContaining(eq("book_5"))).thenReturn(list);
        List<Book> result = bookService.getBooksWithTitle( "book_5");
        assertEquals(1,result.size());
    }

    @Test
    public void filterBookByCategoryTest() throws Exception {
        Book expectedBook = new Book();
        expectedBook.setTitle("book_5");
        expectedBook.setAuthor("abc");
        expectedBook.setCategory("SE");
        expectedBook.setPrice(2.5);
        List<Book> list = new ArrayList<>();
        list.add(expectedBook);
        when(bookRepository.findAllByCategory(eq("SE"))).thenReturn(list);
        List<Book> result = bookService.filterByCategory( "SE");
        assertEquals(1,result.size());
    }

    @Test
    public void filterBookBySellerTest() throws Exception {
        Book expectedBook = new Book();
        Users users = new Users();
        String email = "test";
        users.setEmail(email);
        expectedBook.setUsers(users);
        expectedBook.setTitle("book_5");
        expectedBook.setAuthor("abc");
        expectedBook.setCategory("SE");
        expectedBook.setPrice(2.5);
        List<Book> list = new ArrayList<>();
        list.add(expectedBook);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(users);
        when(bookRepository.findAllByEmailID(Mockito.anyString())).thenReturn(list);
        List<Book> result = bookService.getBooksBySeller( "test");
        assertEquals(1,result.size());
    }

}
