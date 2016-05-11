package com.ryanpmartz.booktrackr.controller;

import com.ryanpmartz.booktrackr.controller.dto.SignupDto;
import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {

    private static final String EMAIL_EXISTS_MESSAGE = "This email is in use";

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignupDto signupDto) {
        final String requestedEmail = signupDto.getEmail();

        Optional<User> existingUserOptional = userService.getUserByEmail(requestedEmail);
        if (existingUserOptional.isPresent()) {
            return ResponseEntity.badRequest().body(EMAIL_EXISTS_MESSAGE);
        }

        User user = new User();
        user.setEnabled(true);
        // never save plain text passwords, we'll address this in the next post
        user.setPassword("*****");
        user.setEmail(signupDto.getEmail());
        user.setFirstName(signupDto.getFirstName());
        user.setLastName(signupDto.getLastName());

        User savedUser = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8).body(savedUser);
    }
}
