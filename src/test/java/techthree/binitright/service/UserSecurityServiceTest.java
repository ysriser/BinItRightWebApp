package techthree.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import techthree.binitright.interfacemethods.UserInterface;
import techthree.binitright.model.User;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSecurityServiceTest {
    @Mock
    private UserInterface userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserSecurityService service;

    @BeforeEach
    void setUp() {
        service = new UserSecurityService();

        try {
            var f1 = UserSecurityService.class.getDeclaredField("userService");
            f1.setAccessible(true);
            f1.set(service, userService);

            var f2 = UserSecurityService.class.getDeclaredField("passwordEncoder");
            f2.setAccessible(true);
            f2.set(service, passwordEncoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User user(String username, String passwordHash) {
        User u = new User();
        u.setUsername(username);
        u.setPassword_hash(passwordHash);
        return u;
    }


    @Test
    void validateUser_whenUsersNull_returnsFalse() {
        when(userService.findByUsername("alice")).thenReturn(null);

        boolean ok = service.validateUser("alice", "pw");

        assertFalse(ok);
        verify(userService).findByUsername("alice");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void validateUser_whenEmptyList_returnsFalse() {
        when(userService.findByUsername("alice")).thenReturn(Collections.emptyList());

        boolean ok = service.validateUser("alice", "pw");

        assertFalse(ok);
        verify(userService).findByUsername("alice");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void validateUser_whenPasswordMatches_returnsTrue() {
        User u = user("alice", "$2a$10$hash");
        when(userService.findByUsername("alice")).thenReturn(List.of(u));
        when(passwordEncoder.matches("pw", "$2a$10$hash")).thenReturn(true);

        boolean ok = service.validateUser("alice", "pw");

        assertTrue(ok);
        verify(userService).findByUsername("alice");
        verify(passwordEncoder).matches("pw", "$2a$10$hash");
    }

    @Test
    void validateUser_whenPasswordDoesNotMatch_returnsFalse() {
        User u = user("alice", "$2a$10$hash");
        when(userService.findByUsername("alice")).thenReturn(List.of(u));
        when(passwordEncoder.matches("wrong", "$2a$10$hash")).thenReturn(false);

        boolean ok = service.validateUser("alice", "wrong");

        assertFalse(ok);
        verify(userService).findByUsername("alice");
        verify(passwordEncoder).matches("wrong", "$2a$10$hash");
    }

    @Test
    void validateUser_whenMultipleUsers_usesFirstOnly() {
        User u1 = user("alice", "hash1");
        User u2 = user("alice", "hash2");

        when(userService.findByUsername("alice")).thenReturn(List.of(u1, u2));
        when(passwordEncoder.matches("pw", "hash1")).thenReturn(true);

        boolean ok = service.validateUser("alice", "pw");

        assertTrue(ok);
        verify(passwordEncoder).matches("pw", "hash1");
        verify(passwordEncoder, never()).matches(anyString(), eq("hash2"));
    }

    @Test
    void generateToken_returnsUrlSafeBase64_withoutPadding_andReasonableLength() {
        String token = service.generateToken();

        assertNotNull(token);
        assertFalse(token.isBlank());

        assertFalse(token.contains("="));

        assertFalse(token.contains("+"));
        assertFalse(token.contains("/"));

        byte[] decoded = Base64.getUrlDecoder().decode(token);
        assertEquals(32, decoded.length); // 32 bytes as per code
    }

    @Test
    void generateToken_twoCalls_returnDifferentTokens() {
        String t1 = service.generateToken();
        String t2 = service.generateToken();

        assertNotEquals(t1, t2);
    }

    @Test
    void saveUser_hashesPassword_andCallsUserServiceSaveUser() {
        User u = user("alice", "plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");

        service.saveUser(u);

        assertEquals("hashedPassword", u.getPassword_hash());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).saveUser(captor.capture());
        assertSame(u, captor.getValue());

        verify(passwordEncoder).encode("plainPassword");
    }
}

