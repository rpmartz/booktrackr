package com.ryanpmartz.booktrackr;

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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(bookController).build();
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setAuthor("John Doe");
        firstBook.setTitle("The First Book");
        firstBook.setNotes("Some notes");

        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setAuthor("Jane Doe");
        secondBook.setTitle("The Second Book");
        secondBook.setNotes("Read this after the first book");

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(firstBook, secondBook));

        // TODO added jayway json path
        mockMvc.perform(get("/books")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].title").value("The First Book"))
                .andExpect(jsonPath("$[1].title").value("The Second Book"))
                .andDo(print());
    }


}
