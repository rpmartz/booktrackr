package com.ryanpmartz.booktrackr.authentication;

import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.domain.UserRole;
import com.ryanpmartz.booktrackr.domain.UserRoleEnum;
import com.ryanpmartz.booktrackr.util.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtParser;
import io.jsonwebtoken.lang.Assert;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Component
public class JwtUtil {

    private static final Logger log = Logger.getLogger(JwtUtil.class);

    private final String signingSecret;

    @Autowired
    public JwtUtil(@Value("${jwt.signing.secret}") String siginingSecret) {
        this.signingSecret = siginingSecret;
    }

    /**
     * Generates a JWT for the user
     *
     * @param user the user to generate a token for
     * @return a signed JWT that will be used for stateless authentication on subsequent requests
     */
    protected String generateToken(User user) {
        checkState(user);

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("user_id", user.getId());
        claims.put("roles", user.getRoles().stream().map(UserRole::getUserRole).collect(toSet()));
        claims.put("email", user.getEmail());

        LocalDate tenDaysFromNow = LocalDate.now().plusDays(10);
        Date expirationDate = DateUtil.toJavaDate(tenDaysFromNow);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, signingSecret)
                .compact();
    }

    protected JwtAuthenticationToken tokenFromStringJwt(String rawJwt) {
        DefaultJwtParser parser = ((DefaultJwtParser) Jwts.parser());
        parser.setSigningKey(signingSecret);

        try {
            Jws<Claims> jws = parser.parseClaimsJws(rawJwt);
            Claims claims = jws.getBody();


            UUID userId = UUID.fromString((String) claims.get("user_id"));
            String email = ((String) claims.get("email"));
            Collection<? extends GrantedAuthority> roles = parseRolesFromClaims(claims);

            return new JwtAuthenticationToken(userId, email, roles);
        } catch (Exception e) {
            log.info(String.format("Exception occurred parsing JWT [%s].\nException message: %s", rawJwt, e.getMessage()));
            return null;
        }
    }

    private void checkState(User user) {
        Assert.notNull(user, "Cannot generate JWT from empty user");
        Assert.isTrue(user.isEnabled());
    }

    @SuppressWarnings("unchecked")
    private Collection<UserRoleEnum> parseRolesFromClaims(Claims claims) {
        List<String> roleValues = (List<String>) claims.get("roles");
        return roleValues.stream().map(UserRoleEnum::valueOf).collect(toSet());
    }
}
