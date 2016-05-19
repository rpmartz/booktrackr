package com.ryanpmartz.booktrackr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanpmartz.booktrackr.authentication.JwtAuthenticationToken;
import com.ryanpmartz.booktrackr.authentication.JwtUtil;
import com.ryanpmartz.booktrackr.domain.Book;
import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.domain.UserRole;
import com.ryanpmartz.booktrackr.domain.UserRoleEnum;
import com.ryanpmartz.booktrackr.service.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class BookControllerTest {

    private static final UUID MISSING_BOOK_ID = UUID.fromString("09c4d7e2-01e5-4dea-a8cd-f3bfc908b316");
    private static final UUID FIRST_BOOK_ID = UUID.fromString("abee7ba3-8bf7-496c-a19a-de0471fc06c1");

    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(bookController).build();

        User userOne = new User();
        userOne.setId(UUID.randomUUID());
        userOne.setEmail("test@booktrackr.com");
        userOne.setEnabled(true);

        UserRole userOneRole = new UserRole();
        userOneRole.setUserRole(UserRoleEnum.ROLE_USER);
        userOneRole.setUser(userOne);

        userOne.setRoles(Collections.singleton(userOneRole));

        JwtUtil jwtUtil = new JwtUtil("secret");
        JwtAuthenticationToken token = jwtUtil.tokenFromStringJwt(jwtUtil.generateToken(userOne));
        SecurityContextHolder.getContext().setAuthentication(token);

        Book firstBook = new Book();
        firstBook.setId(FIRST_BOOK_ID);
        firstBook.setAuthor("John Doe");
        firstBook.setTitle("The First Book");
        firstBook.setNotes("Some notes");
        firstBook.setUser(userOne);

        Book secondBook = new Book();
        secondBook.setId(UUID.fromString("16299861-a686-4489-ad40-5f3578d6bcd9"));
        secondBook.setAuthor("Jane Doe");
        secondBook.setTitle("The Second Book");
        secondBook.setNotes("Read this after the first book");
        secondBook.setUser(userOne);

        when(bookService.getAllBooksForUser(token.getUserId())).thenReturn(Arrays.asList(firstBook, secondBook));
        when(bookService.getBook(firstBook.getId())).thenReturn(Optional.of(firstBook));
        when(bookService.getBook(MISSING_BOOK_ID)).thenReturn(Optional.empty());
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
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
        mockMvc.perform(get("/books/" + FIRST_BOOK_ID)).andExpect(status().isOk())
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

    @Test
    public void testUpdatingBook() throws Exception {
        Map<String, String> json = new HashMap<>();
        json.put("title", "Winnie the Pooh");
        json.put("author", "AA Milne");
        json.put("notes", "Let's Update an Existing Book");

        mockMvc.perform(put("/books/" + FIRST_BOOK_ID).content(mapper.writeValueAsString(json)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(jsonPath("$.author").value("AA Milne"));
    }

    @Test
    public void testUpdateBookJsonIsValidated() throws Exception {
        Map<String, String> json = new HashMap<>();

        mockMvc.perform(put("/books/" + FIRST_BOOK_ID).content(mapper.writeValueAsString(json)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeletingBook() throws Exception {
        mockMvc.perform(delete("/books/" + FIRST_BOOK_ID)).andExpect(status().isOk());
    }
}
