package techthree.binitright.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class RegisterRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void gettersAndSetters_shouldWork() {
        RegisterRequest request = new RegisterRequest();

        request.setUsername("user1");
        request.setPassword("password123");
        request.setEmailAddress("test1@test.com");

        assertEquals("user1", request.getUsername());
        assertEquals("password123", request.getPassword());
        assertEquals("test1@test.com", request.getEmailAddress());
    }

    @Test
    void validation_shouldPass_whenValidInput() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setPassword("password123");
        request.setEmailAddress("user1@test.com");

        Set violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_shouldFail_whenUsernameBlank() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("");
        request.setPassword("password123");
        request.setEmailAddress("user1@test.com");

        Set violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_shouldFail_whenPasswordTooShort() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setPassword("123"); // less than 6 chars
        request.setEmailAddress("user1@test.com");

        Set violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    // (Optional but recommended)
    @Test
    void validation_shouldFail_whenEmailInvalid() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setPassword("password123");
        request.setEmailAddress("not-an-email");

        Set violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}