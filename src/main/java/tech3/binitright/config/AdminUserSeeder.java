package tech3.binitright.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.model.Admin;

@Configuration
public final class AdminUserSeeder {

    private final AdminInterface adminService;

    public AdminUserSeeder(final AdminInterface adminService) {
        this.adminService = adminService;
    }

    @Bean
    @Order(3)
    @Profile({"test", "prod", "default"})
    public CommandLineRunner seedAdmin(final PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminService.findAdminByUsername("admin").isEmpty()) {
                final Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setEmailAddress("admin@binitright.com");
                admin.setName("System Admin");
                admin.setRole("admin");
                
                final String rawPassword = System.getenv("APPUADMINUPASSWORD");
                
                if (rawPassword == null || rawPassword.isEmpty()) {
                    System.out.println("Warning: ADMINUPASSWORD environment variable is missing!");
                } else {
                    admin.setPasswordHash(passwordEncoder.encode(rawPassword));
                    adminService.saveAdmin(admin);
                    System.out.println("Test Admin account seeded successfully.");
                }
            }
        };
    }
}