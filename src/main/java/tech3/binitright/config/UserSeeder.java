package tech3.binitright.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;

@Configuration
public class UserSeeder {
    private final UserInterface userService;

    public UserSeeder(UserInterface userService) {
        this.userService = userService;
    }

    @Bean
    @Profile({"test", "default"}) // Avoid running this in "prod" to keep the DB clean
    public CommandLineRunner seedUsers(PasswordEncoder passwordEncoder) {
        return args -> {
            String testUsername = "User1";

            // 1. Check if user exists to prevent DuplicateKeyException
            if (userService.findByUsername(testUsername).isEmpty()) {
                User user = new User();
                user.setUsername(testUsername);
                user.setEmailAddress("tester@binitright.com");
                user.setName("Default Tester");
                user.setRole("USER");

                // 2. Set a default password for development
                // In a real app, you could also pull this from an env var
                String defaultPassword = "password";
                user.setPassword_hash(passwordEncoder.encode(defaultPassword));

                // 3. Save to database
                userService.saveUser(user);

                System.out.println(">>> User Seeding: '" + testUsername + "' created successfully.");
            } else {
                System.out.println(">>> User Seeding: Test user already exists, skipping.");
            }
        };
    }
}

