package tech3.binitright.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.User;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.service.UserImplementation;

@Configuration
public class AdminUserSeeder {

    @Autowired
    private UserInterface userService;
    @Autowired
    public void setUserService(UserImplementation userserviceImp) {
        this.userService = userserviceImp;
    }

    @Bean
    @Profile({"test","prod","default"}) // Only runs when SPRING_PROFILES_ACTIVE=test
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if the admin already exists to avoid duplicates
            if (userService.findAdminByUsername("admin").isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setEmailAddress("admin@binitright.com");
                admin.setName("System Admin");
                admin.setRole("admin");
                
                // Pull the password from the environment variable (mapped from GitHub Secrets)
                String rawPassword = System.getenv("ADMIN_PASSWORD");
                
                if (rawPassword == null || rawPassword.isEmpty()) {
                    System.out.println("Warning: ADMIN_PASSWORD environment variable is missing!");
                } else {
                    admin.setPassword_hash(passwordEncoder.encode(rawPassword));
                    userService.saveAdmin(admin);
                    System.out.println("Test Admin account seeded successfully.");
                }
            }
        };
    }
}