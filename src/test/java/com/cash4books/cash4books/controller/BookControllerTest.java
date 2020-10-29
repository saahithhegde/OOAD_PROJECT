package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.services.impl.BookServiceImpl;
import com.cash4books.cash4books.services.impl.SessionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookDetailsController.class)
@WithMockUser
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    @MockBean
    SessionServiceImpl sessionService;



    @Test
    public void addBookTest() throws Exception {
        Book book = new Book();
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        String email = "test";
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(bookService.addBook(Mockito.any(Book.class),Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/book/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}")
                .header("email","test")
                .header("Token","dummy")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.title").value("book_5"))
                .andExpect(jsonPath("$.author").value("abc"))
                .andExpect(jsonPath("$.price").value(2.5))
                .andExpect(jsonPath("$.category").value("SE"))
                .andDo(print());
    }
    @Test(expected = Exception.class)
    public void addBookExceptionTest() throws Exception {
        String email = "";
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/book/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}")
                .header("email","test")
                .header("Token","dummy")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void searchBySellerTest() throws Exception {
        Users user1 = new Users();
        user1.setEmail("u1");
        Book book = new Book();
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        book.setUsers(user1);
        List<Book> list = new ArrayList<>();
        list.add(book);
        when(bookService.getBooksBySeller(eq("u1"))).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/seller/u1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].title").exists())
                .andExpect(jsonPath("$.[0].author").exists())
                .andExpect(jsonPath("$.[0].price").exists())
                .andExpect(jsonPath("$.[0].category").exists())
                .andExpect(jsonPath("$.[0].title").value("book_5"))
                .andExpect(jsonPath("$.[0].author").value("abc"))
                .andExpect(jsonPath("$.[0].price").value(2.5))
                .andExpect(jsonPath("$.[0].category").value("SE"))
                .andDo(print());
    }

    @Test
    public void searchByAuthorTest() throws Exception {
        Users user1 = new Users();
        user1.setEmail("u1");
        Book book = new Book();
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        book.setUsers(user1);
        List<Book> list = new ArrayList<>();
        list.add(book);
        when(bookService.filterByAuthor(eq("abc"))).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/author/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].title").exists())
                .andExpect(jsonPath("$.[0].author").exists())
                .andExpect(jsonPath("$.[0].price").exists())
                .andExpect(jsonPath("$.[0].category").exists())
                .andExpect(jsonPath("$.[0].title").value("book_5"))
                .andExpect(jsonPath("$.[0].author").value("abc"))
                .andExpect(jsonPath("$.[0].price").value(2.5))
                .andExpect(jsonPath("$.[0].category").value("SE"))
                .andDo(print());
    }

    @Test
    public void searchByTitleTest() throws Exception {
        Users user1 = new Users();
        user1.setEmail("u1");
        Book book = new Book();
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        book.setUsers(user1);
        List<Book> list = new ArrayList<>();
        list.add(book);
        when(bookService.getBooksWithTitle(eq("book"))).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/title/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].title").exists())
                .andExpect(jsonPath("$.[0].author").exists())
                .andExpect(jsonPath("$.[0].price").exists())
                .andExpect(jsonPath("$.[0].category").exists())
                .andExpect(jsonPath("$.[0].title").value("book_5"))
                .andExpect(jsonPath("$.[0].author").value("abc"))
                .andExpect(jsonPath("$.[0].price").value(2.5))
                .andExpect(jsonPath("$.[0].category").value("SE"))
                .andDo(print());
    }

    @Test
    public void searchByCategoryTest() throws Exception {
        Users user1 = new Users();
        user1.setEmail("u1");
        Book book = new Book();
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        book.setUsers(user1);
        List<Book> list = new ArrayList<>();
        list.add(book);
        when(bookService.filterByCategory(eq("SE"))).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/category/SE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].title").exists())
                .andExpect(jsonPath("$.[0].author").exists())
                .andExpect(jsonPath("$.[0].price").exists())
                .andExpect(jsonPath("$.[0].category").exists())
                .andExpect(jsonPath("$.[0].title").value("book_5"))
                .andExpect(jsonPath("$.[0].author").value("abc"))
                .andExpect(jsonPath("$.[0].price").value(2.5))
                .andExpect(jsonPath("$.[0].category").value("SE"))
                .andDo(print());
    }

}
