package com.ryanpmartz.booktrackr.authentication;

import static java.util.stream.Collectors.toSet;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.junit.Test;

import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.domain.UserRole;
import com.ryanpmartz.booktrackr.domain.UserRoleEnum;
import com.ryanpmartz.booktrackr.util.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtilTest {

    private static final UUID USER_ID = UUID.fromString("4db46fba-1ee5-476d-ad95-06a48bc5ae68");
	private static final String EXPECTED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGJvb2t0cmFja3IuY29tIiwidXNlcl9pZCI6IjRkYjQ2ZmJhLTFlZTUtNDc2ZC1hZDk1LTA2YTQ4YmM1YWU2OCIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJlbWFpbCI6InRlc3RAYm9va3RyYWNrci5jb20iLCJleHAiOjE4MzE5NTcyMDB9.BPeq2lkQSAi61iSHZKJzSrmwRHMbJ9kjmT6e1-NJbD4P_GFcmE4Q-jX6eJWmI40ltCzsTkuOL5XGAxRWz-egag";
    private JwtUtil jwtUtil = new JwtUtil("secret");

    @Test(expected = IllegalArgumentException.class)
    public void testCannotGetTokenForNullUser() {
        jwtUtil.generateToken(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotGetTokenForNonEnabledUser() {
        User user = new User();
        user.setEnabled(false);

        jwtUtil.generateToken(user);
    }

    @Test
    public void testGeneratingJwtForValidUser() {
        User user = buildUser();

        JwtAuthenticationToken tokenFromString = jwtUtil.tokenFromStringJwt(EXPECTED_TOKEN);

        assertTrue(tokenFromString.getEmail().equals(user.getEmail()));
        assertTrue(tokenFromString.getUserId().equals(user.getId()));
        assertTrue(tokenFromString.getAuthorities().containsAll(Collections.singletonList(UserRoleEnum.ROLE_USER)));
    }

    @Test
    public void testParsingRawJwt() {
        JwtAuthenticationToken token = jwtUtil.tokenFromStringJwt(EXPECTED_TOKEN);

        assertTrue(USER_ID.equals(token.getUserId()));
        assertTrue(token.isAuthenticated());
        assertEquals("test@booktrackr.com", token.getEmail());
        assertTrue(token.getAuthorities().size() == 1);
        assertTrue(token.getAuthorities().contains(UserRoleEnum.ROLE_USER));
    }

    @Test
    public void testExpiredTokenFailsValidation() {
        User user = buildUser();

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("user_id", user.getId());
        claims.put("roles", user.getRoles().stream().map(UserRole::getUserRole).collect(toSet()));
        claims.put("email", user.getEmail());

        LocalDate tenDaysFromNow = LocalDate.now().minusDays(10);
        Date expirationDate = DateUtil.toJavaDate(tenDaysFromNow);

        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, "secret")
                .compact();

        assertNull(jwtUtil.tokenFromStringJwt(token));
    }

    private User buildUser() {
        User user = new User();
        user.setEmail("test@booktrackr.com");
        user.setId(USER_ID);
        user.setRoles(new HashSet<>());
        user.setEnabled(true);

        UserRole role = new UserRole();
        role.setUser(user);
        role.setUserRole(UserRoleEnum.ROLE_USER);

        user.getRoles().add(role);

        return user;
    }

}
