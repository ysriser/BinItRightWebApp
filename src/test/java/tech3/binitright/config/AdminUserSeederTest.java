package tech3.binitright.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.model.Admin;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AdminUserSeederTest {

    private AdminUserSeeder adminUserSeeder;
    private AdminInterface adminService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        adminService = Mockito.mock(AdminInterface.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        adminUserSeeder = new AdminUserSeeder(adminService);
    }

    @Test
    void seedAdmin_WhenAdminDoesNotExistAndEnvVarPresent_ShouldSaveAdmin() throws Exception {
        when(adminService.findAdminByUsername("admin")).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        CommandLineRunner runner = adminUserSeeder.seedAdmin(passwordEncoder);
        runner.run();
        String rawPassword = System.getenv("APP_ADMIN_PASSWORD");
        if (rawPassword != null && !rawPassword.isEmpty()) {
            verify(adminService, times(1)).saveAdmin(any(Admin.class));
            verify(passwordEncoder).encode(rawPassword);
        } else {
            verify(adminService, never()).saveAdmin(any(Admin.class));
        }
    }

    @Test
    void seedAdmin_WhenAdminAlreadyExists_ShouldNotSave() throws Exception {

        when(adminService.findAdminByUsername("admin")).thenReturn(List.of(new Admin()));
        CommandLineRunner runner = adminUserSeeder.seedAdmin(passwordEncoder);
        runner.run();
        verify(adminService, never()).saveAdmin(any(Admin.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
