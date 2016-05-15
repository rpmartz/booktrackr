package com.ryanpmartz.booktrackr.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanpmartz.booktrackr.authentication.dto.LoginRequest;
import com.ryanpmartz.booktrackr.domain.User;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger log = Logger.getLogger(JwtLoginFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtLoginFilter(String defaultFilterProcessesUrl, JwtUtil jwtUtil, UserDetailsService userDetailsService, AuthenticationManager authManager) {
        super(defaultFilterProcessesUrl);

        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;

        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        final LoginRequest login = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

        log.debug("Attempting authentication for username [" + login.getUsername() + "]");
        final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                login.getUsername(), login.getPassword());

        return getAuthenticationManager().authenticate(loginToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {

        log.debug("Successful authentication occurred. Auth name: " + authResult.getName());

        User authenticatedUser = (User) userDetailsService.loadUserByUsername(authResult.getName());
        String token = jwtUtil.generateToken(authenticatedUser);
        response.setHeader("Authorization", "Bearer " + token);

        SecurityContextHolder.getContext().setAuthentication(jwtUtil.tokenFromStringJwt(token));
    }


}
