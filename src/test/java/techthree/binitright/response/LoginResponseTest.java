package techthree.binitright.response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginResponseTest {
    @Test
    void defaultConstructor_andSetters_shouldWork() {
        LoginResponse response = new LoginResponse();

        response.setSuccess(true);
        response.setMessage("Login successful");
        response.setToken("abc123token");

        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertEquals("abc123token", response.getToken());
    }

    @Test
    void parameterizedConstructor_shouldSetFieldsCorrectly() {
        LoginResponse response =
                new LoginResponse(false, "Invalid credentials", null);

        assertFalse(response.isSuccess());
        assertEquals("Invalid credentials", response.getMessage());
        assertNull(response.getToken());
    }
}
