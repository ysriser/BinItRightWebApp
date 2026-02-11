package tech3.binitright.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech3.binitright.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // This tells Spring to load the context and inject @Values
class JwtUtilTest {

    @Autowired // Let Spring provide the fully-configured bean
    private JwtUtil jwtUtil;

    @Test
    void generateTokenAndExtractSubjectFromUserId() {
        final User user = new User();
        user.setId(123L);
        user.setUsername("alice");
        user.setRole("USER");

        final String token = jwtUtil.generateToken(user);
        final String subject = jwtUtil.extractUsername(token);

        assertTrue(token.length() > 20);
        assertEquals("123", subject);
    }

    @Test
    void invalidTokenThrowsException() {
        // Note: Jwts parser throws specific exceptions like MalformedJwtException
        assertThrows(Exception.class, () -> jwtUtil.extractUsername("invalid-token"));
    }
}