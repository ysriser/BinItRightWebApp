package tech3.binitright.util;

import org.junit.jupiter.api.Test;
import tech3.binitright.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

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
        assertThrows(RuntimeException.class, () -> jwtUtil.extractUsername("invalid-token"));
    }
}
