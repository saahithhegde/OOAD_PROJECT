package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.book.BookDtoQuery;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.BookRepository;
import com.cash4books.cash4books.services.impl.BookServiceImpl;
import com.cash4books.cash4books.services.impl.SessionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    @MockBean
    BookRepository bookRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;



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
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some test".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/book/add")
                .file(firstFile).header("Token","dummy")
                .param("book", "{\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}"))
                .andExpect(status().isAccepted());
    }
    @Test
    public void addBookExceptionTest() throws Exception {
        Book book = new Book();
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        String email = "test";
        when(sessionService.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(email);
        when(bookService.addBook(Mockito.any(Book.class),Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenThrow(Exception.class);
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some test".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/book/add")
                .file(firstFile).header("Token","dummy")
                .param("book", "{\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}"))
                .andExpect(status().is4xxClientError());
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

    @Test
    public void searchIsbnTest() throws Exception {
        Book book = new Book();
        Users user1 = new Users();
        user1.setEmail("u1");
        book.setIsbn("1234");
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        book.setUsers(user1);
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(bookService.getBooksByIsbn(Mockito.anyString())).thenReturn(bookList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/isbn/1234")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].isbn").exists())
                .andExpect(jsonPath("$.[0].isbn").value("1234"));
    }

    @Test
    public void getSellerBooksTest() throws Exception {
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
        when(bookService.getBooksOfSeller(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/seller/books")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","dummy")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].title").exists())
                .andExpect(jsonPath("$.[0].author").exists())
                .andExpect(jsonPath("$.[0].price").exists())
                .andExpect(jsonPath("$.[0].category").exists())
                .andExpect(jsonPath("$.[0].title").value("book_5"))
                .andExpect(jsonPath("$.[0].author").value("abc"))
                .andExpect(jsonPath("$.[0].price").value(2.5))
                .andExpect(jsonPath("$.[0].category").value("SE"));
    }

    @Test
    public void getSellerBooksExceptionTest() throws Exception {
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
        when(bookService.getBooksOfSeller(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenThrow(Exception.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/seller/books")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","dummy")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getDistinctIsbnTest() throws Exception {
        BookDtoQuery bookDtoQuery = new BookDtoQuery("1234","book_5","abc","SE","description",null,1L);

        List<BookDtoQuery> bookList = new ArrayList<>();
        bookList.add(bookDtoQuery);
        when(bookService.fetchAllDistinctIsbn()).thenReturn(bookList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/getDistinctIsbn")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","dummy")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

}
