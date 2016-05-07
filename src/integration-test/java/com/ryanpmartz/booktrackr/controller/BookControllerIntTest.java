package com.ryanpmartz.booktrackr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanpmartz.booktrackr.BooktrackrApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BooktrackrApplication.class)
@WebAppConfiguration
@Sql("/integration-test-data.sql")
public class BookControllerIntTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testGetAllBooks() throws Exception {

        mockMvc.perform(get("/books")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].title").value("1984"))
                .andExpect(jsonPath("$[1].title").value("The Great Gatsby"))
                .andDo(print());
    }

    @Test
    public void testGetBookById() throws Exception {
        mockMvc.perform(get("/books/09c4d7e2-01e5-4dea-a8cd-f3bfc908b316")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.title").value("1984"))
                .andDo(print());
    }

    @Test
    public void testCreatingBook() throws Exception {
        Map<String, String> json = new HashMap<>();
        json.put("author", "Dave Ramsey");
        json.put("title", "Entreleadership");

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(json)))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void testUpdatingBook() throws Exception {
        Map<String, String> json = new HashMap<>();
        json.put("author", "Dave Ramsey");
        json.put("title", "Entreleadership");
        json.put("notes", "Listened to the audio version");

        mockMvc.perform(put("/books/09c4d7e2-01e5-4dea-a8cd-f3bfc908b316").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(json)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(json.get("author")))
                .andExpect(jsonPath("$.title").value(json.get("title")))
                .andExpect(jsonPath("$.notes").value(json.get("notes")));
    }

    @Test
    public void testDeletingBook() throws Exception {
        mockMvc.perform(delete("/books/09c4d7e2-01e5-4dea-a8cd-f3bfc908b316")).andExpect(status().isOk());
        mockMvc.perform(get("/books/09c4d7e2-01e5-4dea-a8cd-f3bfc908b316")).andExpect(status().isNotFound());
    }
}
