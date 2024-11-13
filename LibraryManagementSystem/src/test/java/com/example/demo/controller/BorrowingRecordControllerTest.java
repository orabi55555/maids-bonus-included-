package com.example.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.dto.BookDto;
import com.example.demo.model.dto.BorrowingRecordDto;
import com.example.demo.model.dto.PatronDto;
import com.example.demo.service.impl.BorrowingRecordServiceImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(BorrowingRecordController.class)
public class BorrowingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordServiceImpl borrowingRecordService;
    

    
    @MockBean
    private UserDetailsService userDetailsService;
    
    private BorrowingRecordDto borrowingRecordDto;
    private BookDto book;
    private PatronDto patron;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    public void setup() {

        book = new BookDto();
        book.setId(1L);
        book.setTitle("Book1");
        book.setAuthor("Author1");
        book.setIsbn("123456789x");
        book.setPublicationYear(2020);

        patron = new PatronDto();
        patron.setId(1L);
        patron.setName("Patron1");
        patron.setEmail("testEmail@test.com");
        patron.setPhoneNumber("01222222222");

        borrowingRecordDto = new BorrowingRecordDto();
        borrowingRecordDto.setId(1L);
        borrowingRecordDto.setBook(book);
        borrowingRecordDto.setPatron(patron);
        borrowingRecordDto.setBorrowDate(LocalDateTime.of(2024, 4, 10, 10, 0, 0));
        borrowingRecordDto.setReturnDate(null);
        
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateBorrowingRecord() throws Exception {
        when(borrowingRecordService.borrowBook(anyLong(), anyLong())).thenReturn(borrowingRecordDto);

        mockMvc.perform(post("/borrow/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.book.id").value(1L))
                .andExpect(jsonPath("$.patron.id").value(1L))
                .andExpect(jsonPath("$.borrowDate").value(borrowingRecordDto.getBorrowDate().format(formatter)));

        verify(borrowingRecordService, times(1)).borrowBook(anyLong(), anyLong());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdateBorrowingRecordById() throws Exception {
        borrowingRecordDto.setReturnDate(LocalDateTime.of(2024, 8, 9, 10, 0, 0));
        when(borrowingRecordService.returnBook(anyLong(), anyLong())).thenReturn(borrowingRecordDto);

        mockMvc.perform(put("/return/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.book.id").value(1L))
                .andExpect(jsonPath("$.patron.id").value(1L))
                .andExpect(jsonPath("$.borrowDate").value(borrowingRecordDto.getBorrowDate().format(formatter)))
                .andExpect(jsonPath("$.returnDate").value(borrowingRecordDto.getReturnDate().format(formatter)));

        verify(borrowingRecordService, times(1)).returnBook(anyLong(), anyLong());
    }
    

}
