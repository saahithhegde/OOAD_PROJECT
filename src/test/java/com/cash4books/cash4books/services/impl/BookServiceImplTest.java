package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.BookRepository;
import com.cash4books.cash4books.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnBook() throws Exception {
        Book expectedBook = new Book();
        expectedBook.setTitle("book_5");
        expectedBook.setAuthor("abc");
        expectedBook.setCategory("SE");
        expectedBook.setPrice(2.5);
        Users users = new Users();
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(users);
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        Book actualBook = bookService.addBook(expectedBook,"test");
        assertEquals(expectedBook.getTitle(),actualBook.getTitle());
        assertEquals(expectedBook.getAuthor(),actualBook.getAuthor());
        assertEquals(expectedBook.getCategory(),actualBook.getCategory());
        assertEquals(expectedBook.getPrice(),actualBook.getPrice(),0);
    }

    @Test(expected = Exception.class)
    public void shouldThrowException() throws Exception {
        Book expectedBook = new Book();
        expectedBook.setTitle("book_5");
        expectedBook.setAuthor("abc");
        expectedBook.setCategory("SE");
        expectedBook.setPrice(2.5);
        Users users = new Users();
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(users);
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        Book actualBook = bookService.addBook(expectedBook,"");

    }

}
