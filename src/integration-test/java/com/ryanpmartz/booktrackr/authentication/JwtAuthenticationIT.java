package com.ryanpmartz.booktrackr.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanpmartz.booktrackr.BooktrackrApplication;
import com.ryanpmartz.booktrackr.authentication.dto.LoginRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StringUtils.hasText;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BooktrackrApplication.class})
@WebIntegrationTest(randomPort = true)
@Sql("/integration-test-data.sql")
public class JwtAuthenticationIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    /**
     * Test to ensure that `/books` is denied to an unauthenticated user but can be accessed
     * once authenticated.
     *
     * @throws Exception
     */
    @Test
    public void testCanAuthenticateAndUseTokenToAccessProtectedResource() throws Exception {
        mockMvc.perform(get("/books")).andExpect(status().isForbidden());

        LoginRequest login = new LoginRequest();
        login.setUsername("booktrackr@ryanpmartz.com");
        login.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();

        MvcResult authenticationResult = mockMvc.perform(post("/authenticate").content(mapper.writeValueAsString(login)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        String authHeaderValue = authenticationResult.getResponse().getHeader("Authorization");
        assertTrue(hasText(authHeaderValue));

        mockMvc.perform(get("/books").header("Authorization", authHeaderValue))
                .andDo(print()).andExpect(status().isOk());
    }

}
