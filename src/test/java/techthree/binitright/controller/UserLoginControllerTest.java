package techthree.binitright.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import techthree.binitright.interfacemethods.UserInterface;
import techthree.binitright.model.User;
import techthree.binitright.request.LoginRequest;
import techthree.binitright.request.RegisterRequest;
import techthree.binitright.response.LoginResponse;
import techthree.binitright.response.RegisterResponse;
import techthree.binitright.util.JwtUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserLoginControllerTest {

    private UserLoginController controller;
    private UserInterface userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        controller = new UserLoginController();
        userService = Mockito.mock(UserInterface.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtUtil = Mockito.mock(JwtUtil.class);

        ReflectionTestUtils.setField(controller, "userService", userService);
        ReflectionTestUtils.setField(controller, "passwordEncoder", passwordEncoder);
        ReflectionTestUtils.setField(controller, "jwtUtil", jwtUtil);
    }

    @Test
    void login_Success_ReturnsToken() {

        LoginRequest request = new LoginRequest();
        request.setUsername("tester");
        request.setPassword("password123");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setPassword_hash("hashed_pass");

        when(userService.findByUsername("tester")).thenReturn(List.of(mockUser));
        when(passwordEncoder.matches("password123", "hashed_pass")).thenReturn(true);
        when(jwtUtil.generateToken(mockUser)).thenReturn("mock-jwt-token");

        // Act
        LoginResponse response = controller.login(request);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("Login success", response.getMessage());
    }

    @Test
    void register_NewUser_ReturnsOk() {

        RegisterRequest req = new RegisterRequest();
        req.setUsername("newUser");
        req.setPassword("pass123");

        when(userService.existsByUsername("newUser")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encoded_pass");

        // Act
        ResponseEntity<RegisterResponse> response = controller.register(req);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    void register_ExistingUser_ReturnsBadRequest() {

        RegisterRequest req = new RegisterRequest();
        req.setUsername("existingUser");
        req.setPassword("pass123");

        when(userService.existsByUsername("existingUser")).thenReturn(true);

        // Act
        ResponseEntity<RegisterResponse> response = controller.register(req);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        verify(userService, never()).saveUser(any(User.class));
    }
}