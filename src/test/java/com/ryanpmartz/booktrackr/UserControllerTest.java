package com.ryanpmartz.booktrackr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanpmartz.booktrackr.controller.UserController;
import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class UserControllerTest {

    private static final String EXISTING_USER_EMAIL = "existinguser@ryanpmartz.com";
    private static final String NEW_USER_EMAIL = "newuser@ryanpmartz.com";

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, String> requestJsonData = new HashMap<>();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(userController).build();

        requestJsonData.put("firstName", "New");
        requestJsonData.put("lastName", "User");
        requestJsonData.put("password", "he#4!09ql)C");
        requestJsonData.put("confirmPassword", "he#4!09ql)C");

        when(userService.getUserByEmail(EXISTING_USER_EMAIL)).thenReturn(Optional.of(new User()));
        when(userService.getUserByEmail(NEW_USER_EMAIL)).thenReturn(Optional.empty());
    }

    @Test
    public void testCreatingAccount() throws Exception {
        User expectedUser = new User();
        expectedUser.setEmail(NEW_USER_EMAIL);
        expectedUser.setPassword("super secret");

        when(userService.createUser(any(User.class))).thenReturn(expectedUser);

        requestJsonData.put("email", NEW_USER_EMAIL);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(requestJsonData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(NEW_USER_EMAIL))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void testCreatingAccountWithEmailInUse() throws Exception {
        requestJsonData.put("email", EXISTING_USER_EMAIL);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(requestJsonData)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(User.class));
    }
}
