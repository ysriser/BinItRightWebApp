package tech3.binitright.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LoginRequestTest {
    @Test
    void defaultConstructor_andSetters_shouldWork() {
        LoginRequest request = new LoginRequest();

        request.setUsername("user1");
        request.setPassword("password");

        assertEquals("user1", request.getUsername());
        assertEquals("password", request.getPassword());
    }

    @Test
    void parameterizedConstructor_shouldSetFieldsCorrectly() {
        LoginRequest request = new LoginRequest("admin", "admin123");

        assertEquals("admin", request.getUsername());
        assertEquals("admin123", request.getPassword());
    }
}
