package tech3.binitright.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.model.Admin;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminSecurityServiceTest {
    @Mock
    private AdminInterface adminInterface;

    @InjectMocks
    private AdminSecurityService adminSecurityService;

    // ---------- helper ----------
    private Admin admin(String username, String passwordHash) {
        Admin a = new Admin();
        a.setUsername(username);
        a.setPassword_hash(passwordHash);
        return a;
    }

    // ---------- tests ----------

    @Test
    void loadUserByUsername_whenAdminExists_returnsUserDetails() {
        Admin admin = admin("admin1", "$2a$10$encrypted");
        when(adminInterface.findAdminByUsername("admin1"))
                .thenReturn(List.of(admin));

        UserDetails userDetails =
                adminSecurityService.loadUserByUsername("admin1");

        assertNotNull(userDetails);
        assertEquals("admin1", userDetails.getUsername());
        assertEquals("$2a$10$encrypted", userDetails.getPassword());

        // role check
        assertTrue(
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(role -> role.equals("ROLE_admin"))
        );

        verify(adminInterface).findAdminByUsername("admin1");
    }

    @Test
    void loadUserByUsername_whenMultipleAdmins_returnsFirst() {
        Admin admin1 = admin("admin1", "hash1");
        Admin admin2 = admin("admin1", "hash2");

        when(adminInterface.findAdminByUsername("admin1"))
                .thenReturn(List.of(admin1, admin2));

        UserDetails userDetails =
                adminSecurityService.loadUserByUsername("admin1");

        assertEquals("admin1", userDetails.getUsername());
        assertEquals("hash1", userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_whenEmptyList_throwsException() {
        when(adminInterface.findAdminByUsername("missing"))
                .thenReturn(Collections.emptyList());

        UsernameNotFoundException ex =
                assertThrows(UsernameNotFoundException.class,
                        () -> adminSecurityService.loadUserByUsername("missing"));

        assertTrue(ex.getMessage().contains("Admin not found"));
        verify(adminInterface).findAdminByUsername("missing");
    }

    @Test
    void loadUserByUsername_whenNullList_throwsException() {
        when(adminInterface.findAdminByUsername("nullUser"))
                .thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> adminSecurityService.loadUserByUsername("nullUser"));

        verify(adminInterface).findAdminByUsername("nullUser");
    }
}


