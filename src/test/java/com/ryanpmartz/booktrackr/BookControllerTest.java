package com.ryanpmartz.booktrackr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanpmartz.booktrackr.controller.BookController;
import com.ryanpmartz.booktrackr.domain.Book;
import com.ryanpmartz.booktrackr.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class BookControllerTest {

    private static final UUID MISSING_BOOK_ID = UUID.fromString("09c4d7e2-01e5-4dea-a8cd-f3bfc908b316");

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(bookController).build();

        Book firstBook = new Book();
        firstBook.setId(UUID.fromString("abee7ba3-8bf7-496c-a19a-de0471fc06c1"));
        firstBook.setAuthor("John Doe");
        firstBook.setTitle("The First Book");
        firstBook.setNotes("Some notes");

        Book secondBook = new Book();
        secondBook.setId(UUID.fromString("16299861-a686-4489-ad40-5f3578d6bcd9"));
        secondBook.setAuthor("Jane Doe");
        secondBook.setTitle("The Second Book");
        secondBook.setNotes("Read this after the first book");

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(firstBook, secondBook));
        when(bookService.getBook(firstBook.getId())).thenReturn(Optional.of(firstBook));
        when(bookService.getBook(MISSING_BOOK_ID)).thenReturn(Optional.empty());
    }

    @Test
    public void testGetAllBooks() throws Exception {

        mockMvc.perform(get("/books")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].title").value("The First Book"))
                .andExpect(jsonPath("$[1].title").value("The Second Book"));
    }

    @Test
    public void testGetBookById() throws Exception {
        mockMvc.perform(get("/books/abee7ba3-8bf7-496c-a19a-de0471fc06c1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.title").value("The First Book"));

        mockMvc.perform(get("/books/" + MISSING_BOOK_ID)).andExpect(status().isNotFound());
    }

    @Test
    public void testCreateBookWithValidJson() throws Exception {
        Map<String, String> json = new HashMap<>();
        json.put("title", "Winnie the Pooh");
        json.put("author", "AA Milne");

        mockMvc.perform(post("/books")
                .content(new ObjectMapper().writeValueAsString(json)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void testThatBookJsonIsValidated() throws Exception {
        Map<String, String> json = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/books")
                        .content(mapper.writeValueAsString(json))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        json.put("author", "Lee Child");

        mockMvc.perform(post("/books")
                .content(mapper.writeValueAsString(json))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        json.remove("author");
        json.put("title", "61 Hours");

        mockMvc.perform(post("/books")
                .content(mapper.writeValueAsString(json))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }
}
