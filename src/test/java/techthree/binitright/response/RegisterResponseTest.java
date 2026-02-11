package techthree.binitright.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class RegisterResponseTest {
    @Test
    void constructor_shouldSetFieldsCorrectly_whenSuccessTrue() {
        RegisterResponse response =
                new RegisterResponse(true, "Registered successfully");

        assertTrue(response.isSuccess());
        assertEquals("Registered successfully", response.getMessage());
    }

    @Test
    void constructor_shouldSetFieldsCorrectly_whenSuccessFalse() {
        RegisterResponse response =
                new RegisterResponse(false, "Registration failed");

        assertFalse(response.isSuccess());
        assertEquals("Registration failed", response.getMessage());
    }
}
