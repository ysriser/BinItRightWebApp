package techthree.binitright.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    private LoginController controller;

    @BeforeEach
    void setUp() {
        // Instantiate the controller manually
        controller = new LoginController();
    }

    @Test
    void login_WhenNotAuthenticated_ReturnsLoginPage() {

        String viewName = controller.login(null);

        // Assert
        assertEquals("login", viewName);
    }

    @Test
    void login_WhenAlreadyAuthenticated_RedirectsToDashboard() {

        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);

        // Act
        String viewName = controller.login(auth);

        // Assert
        assertEquals("redirect:/admin/dashboard", viewName);
    }

    @Test
    void login_WhenAuthenticationObjectExistsButNotAuthenticated_ReturnsLoginPage() {

        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);

        // Act
        String viewName = controller.login(auth);

        // Assert
        assertEquals("login", viewName);
    }
}
