package com.ryanpmartz.booktrackr.authentication;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final Logger log = Logger.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;

        log.debug("Attempting JWT Authentication");
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            log.debug("Missing or invalid Authorization header");
            chain.doFilter(request, response);
            return;
        }

        String authToken = header.substring(7);
        JwtAuthenticationToken token = jwtUtil.tokenFromStringJwt(authToken);

        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(req, response);
    }

}