package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.cart.CartDTO;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.services.impl.CartServiceImpl;
import com.cash4books.cash4books.services.impl.SessionServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CartController.class)
@WithMockUser
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartServiceImpl cartService;


    @Test
    public void addBookTest() throws Exception {
        CartDTO cartDTO = new CartDTO();
        Book book = new Book();
        book.setBookID(1);
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        String email = "test";
        cartDTO.setBookID(1);
        cartDTO.setEmail(email);
        cartDTO.setBookDetails(book);

        when(cartService.addToCart(Mockito.any(Book.class),Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(cartDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookID\": 1,\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}")
                .header("Token","test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.bookID").exists())
                .andExpect(jsonPath("$.email").value("test"))
                .andExpect(jsonPath("$.bookID").value(1));
    }

    @Test
    public void addBookUserNotLoggedInTest() throws Exception {
        when(cartService.addToCart(Mockito.any(Book.class),Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenThrow(Exception.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookID\": 1,\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}")
                .header("Token","test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteBookTest() throws Exception {
        CartDTO cartDTO = new CartDTO();
        Book book = new Book();
        book.setBookID(1);
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        String email = "test";
        cartDTO.setBookID(1);
        cartDTO.setEmail(email);
        cartDTO.setBookDetails(book);

        when(cartService.deleteFromCart(Mockito.any(Book.class),Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(cartDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookID\": 1,\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}")
                .header("Token","test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.bookID").exists())
                .andExpect(jsonPath("$.email").value("test"))
                .andExpect(jsonPath("$.bookID").value(1));
    }

    @Test
    public void deleteBookUserNotLoggedInTest() throws Exception {
        when(cartService.deleteFromCart(Mockito.any(Book.class),Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenThrow(Exception.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookID\": 1,\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}")
                .header("Token","test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getUserCartTest() throws Exception {
        CartDTO cartDTO = new CartDTO();
        Book book = new Book();
        book.setBookID(1);
        book.setTitle("book_5");
        book.setAuthor("abc");
        book.setCategory("SE");
        book.setPrice(2.5);
        String email = "test";
        cartDTO.setBookID(1);
        cartDTO.setEmail(email);
        cartDTO.setBookDetails(book);
        List<CartDTO> cartDTOList = new ArrayList<>();
        cartDTOList.add(cartDTO);
        when(cartService.getUserCart(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(cartDTOList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/getUserCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookID\": 1,\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}")
                .header("Token","test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].email").exists())
                .andExpect(jsonPath("$.[0].bookID").exists())
                .andExpect(jsonPath("$.[0].email").value("test"))
                .andExpect(jsonPath("$.[0].bookID").value(1));
    }

    @Test
    public void getUserCartNotloggedInTest() throws Exception {
        when(cartService.getUserCart(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenThrow(Exception.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/getUserCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookID\": 1,\"title\":\"book_5\", \"author\" : \"abc\",\"price\" : 2.5,\"category\" : \"SE\"}")
                .header("Token","test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
