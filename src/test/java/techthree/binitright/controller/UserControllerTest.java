package techthree.binitright.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import techthree.binitright.interfacemethods.UserInterface;
import techthree.binitright.model.User;
import techthree.binitright.response.UserProfileResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserController controller;
    private UserInterface userService;

    @BeforeEach
    void setUp() {

        controller = new UserController();


        userService = Mockito.mock(UserInterface.class);


        ReflectionTestUtils.setField(controller, "userService", userService);
    }

    @Test
    void getUserProfile_UserExists_ReturnsOkWithProfile() {

        Long userId = 101L;
        User mockUser = new User();
        mockUser.setUsername("test_user");
        when(userService.findById(userId)).thenReturn(mockUser);

        ResponseEntity<UserProfileResponse> response = controller.getUserProfile(userId);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getUserProfile_UserDoesNotExist_ReturnsNotFound() {

        Long userId = 99L;
        when(userService.findById(userId)).thenReturn(null);


        ResponseEntity<UserProfileResponse> response = controller.getUserProfile(userId);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}