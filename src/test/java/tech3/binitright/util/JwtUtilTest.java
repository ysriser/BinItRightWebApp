package tech3.binitright.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.model.User;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // This manually injects the secret WITHOUT needing Spring to start
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "this-is-a-fake-key-for-testing-jwt-token-validation");
    }

    @Test
    void generateTokenAndExtractSubjectFromUserId() {
        User user = new User();
        user.setId(123L);
        user.setUsername("alice");
        user.setRole("USER");

        String token = jwtUtil.generateToken(user);
        String subject = jwtUtil.extractUsername(token);

        assertNotNull(token);
        assertEquals("123", subject);
    }
}