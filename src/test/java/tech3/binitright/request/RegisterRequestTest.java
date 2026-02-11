package tech3.binitright.request;

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

        request.setUsername("sujitha");
        request.setPassword("password123");

        assertEquals("sujitha", request.getUsername());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void validation_shouldPass_whenValidInput() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setPassword("password123");

        Set violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_shouldFail_whenUsernameBlank() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("");
        request.setPassword("password123");

        Set violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_shouldFail_whenPasswordTooShort() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setPassword("123"); // less than 6 chars

        Set violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}

