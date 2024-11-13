package com.example.demo.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.dto.BookDto;
import com.example.demo.service.impl.BookServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    
    private BookDto bookDto;
    private String jwtToken;
    
    @Value("${jwt.secret}")
    private String SECRET_KEY;


    @BeforeEach
    public void setup() {

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setAuthor("Harper Lee");
        bookDto.setTitle("To Kill a Mockingbird");
        bookDto.setIsbn("1234567890");
        bookDto.setPublicationYear(1960); 
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateBook() throws Exception {
        when(bookService.createBook(any(BookDto.class))).thenReturn(bookDto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"Harper Lee\",\"title\":\"To Kill a Mockingbird\",\"isbn\":\"1234567890\",\"publicationYear\":1960}")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Harper Lee"))
                .andExpect(jsonPath("$.title").value("To Kill a Mockingbird"))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.publicationYear").value(1960));

        verify(bookService, times(1)).createBook(any(BookDto.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(bookDto));

        mockMvc.perform(get("/books").header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Harper Lee"))
                .andExpect(jsonPath("$[0].title").value("To Kill a Mockingbird"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(bookDto);

        mockMvc.perform(get("/books/1").header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Harper Lee"))
                .andExpect(jsonPath("$.title").value("To Kill a Mockingbird"));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdateBookById() throws Exception {
        when(bookService.updateBookById(eq(1L), any(BookDto.class))).thenReturn(bookDto);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"Harper Lee\",\"title\":\"To Kill a Mockingbird\",\"isbn\":\"1234567890\",\"publicationYear\":1960}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Harper Lee"))
                .andExpect(jsonPath("$.title").value("To Kill a Mockingbird"));

        verify(bookService, times(1)).updateBookById(eq(1L), any(BookDto.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteBookById() throws Exception {
        doNothing().when(bookService).deleteBookById(1L);

        mockMvc.perform(delete("/books/1").header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteBookById(1L);
    }
    
    
}
