package tech3.binitright.response;

import org.junit.jupiter.api.Test;
import tech3.binitright.model.User;

import static org.junit.jupiter.api.Assertions.*;
public class UserProfileResponseTest {
    @Test
    void defaultConstructor_andSetters_shouldWork() {
        UserProfileResponse response = new UserProfileResponse();

        response.setId(1L);
        response.setPointBalance(500);

        assertEquals(1L, response.getId());
        assertEquals(500, response.getPointBalance());
    }

    @Test
    void constructorWithUser_shouldMapFieldsCorrectly() {
        // Create a real User object (no mocking needed if it's simple)
        User user = new User();
        user.setId(10L);
        user.setPointBalance(1000);

        UserProfileResponse response = new UserProfileResponse(user);

        assertEquals(10L, response.getId());
        assertEquals(1000, response.getPointBalance());
    }
}
