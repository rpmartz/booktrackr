package com.ryanpmartz.booktrackr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanpmartz.booktrackr.authentication.JwtUtil;
import com.ryanpmartz.booktrackr.controller.dto.SignupDto;
import com.ryanpmartz.booktrackr.controller.dto.UserDto;
import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.domain.UserRole;
import com.ryanpmartz.booktrackr.domain.UserRoleEnum;
import com.ryanpmartz.booktrackr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private static final String EMAIL_EXISTS_MESSAGE = "This email is in use";

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService, final PasswordEncoder encoder, final JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignupDto signupDto) throws JsonProcessingException {
        if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
            Map<String, String> errorJson = new HashMap<>();
            errorJson.put("field", "confirmPassword");
            errorJson.put("error", "Password and confirm password must match");

            ObjectMapper mapper = new ObjectMapper();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON_UTF8).body(mapper.writeValueAsString(errorJson));
        }

        final String requestedEmail = signupDto.getEmail();

        Optional<User> existingUserOptional = userService.getUserByEmail(requestedEmail);
        if (existingUserOptional.isPresent()) {
            return ResponseEntity.badRequest().body(EMAIL_EXISTS_MESSAGE);
        }

        User user = new User();
        user.setEnabled(true);
        user.setEmail(signupDto.getEmail());
        user.setFirstName(signupDto.getFirstName());
        user.setLastName(signupDto.getLastName());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setRoles(new HashSet<>());

        UserRole roleUser = new UserRole();
        roleUser.setUser(user);
        roleUser.setUserRole(UserRoleEnum.ROLE_USER);
        user.getRoles().add(roleUser);

        signupDto.setPassword(null);
        signupDto.setConfirmPassword(null);

        User savedUser = userService.createUser(user);
        String jwt = jwtUtil.generateToken(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + jwt)
                .body(new UserDto(savedUser));
    }
}
