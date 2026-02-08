package tech3.binitright.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.service.UserImplementation;

@Configuration
public class AdminUserSeeder {

    private final AdminInterface adminService;

    public AdminUserSeeder(AdminInterface adminService) {
        this.adminService = adminService;
    }
    @Bean
    @Order(3)
    @Profile({"test","prod","default"}) // Only runs when SPRINGUPROFILESUACTIVE=test
    public CommandLineRunner seedAdmin(PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if the admin already exists to avoid duplicates
            if (adminService.findAdminByUsername("admin").isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setEmailAddress("admin@binitright.com");
                admin.setName("System Admin");
                admin.setRole("admin");
                
                // Pull the password from the environment variable (mapped from GitHub Secrets)
                String rawPassword = System.getenv("APPUADMINUPASSWORD");
                
                if (rawPassword == null || rawPassword.isEmpty()) {
                    System.out.println("Warning: ADMINUPASSWORD environment variable is missing!");
                } else {
                    admin.setPasswordUhash(passwordEncoder.encode(rawPassword));
                    adminService.saveAdmin(admin);
                    System.out.println("Test Admin account seeded successfully.");
                }
            }
        };
    }
}