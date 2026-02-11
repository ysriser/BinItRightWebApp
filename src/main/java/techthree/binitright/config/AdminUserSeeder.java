package techthree.binitright.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import techthree.binitright.interfacemethods.AdminInterface;
import techthree.binitright.model.Admin;


@Configuration
public class AdminUserSeeder {

    private static final Logger log = LoggerFactory.getLogger(AdminUserSeeder.class);

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_ROLE = "admin";
    private final AdminInterface adminService;

    public AdminUserSeeder(AdminInterface adminService) {
        this.adminService = adminService;
    }
    @Bean
    @Order(3)
    @Profile({"test","prod","default"}) // Only runs when SPRING_PROFILES_ACTIVE=test
    public CommandLineRunner seedAdmin(PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if the admin already exists to avoid duplicates
            if (adminService.findAdminByUsername(ADMIN_USERNAME).isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername(ADMIN_USERNAME);
                admin.setEmailAddress("admin@binitright.com");
                admin.setName("System Admin");
                admin.setRole(ADMIN_ROLE);
                
                // Pull the password from the environment variable (mapped from GitHub Secrets)
                String rawPassword = System.getenv("APP_ADMIN_PASSWORD");
                
                if (rawPassword == null || rawPassword.isEmpty()) {
                    log.warn("Warning: ADMIN_PASSWORD environment variable is missing!");
                } else {
                    admin.setPassword_hash(passwordEncoder.encode(rawPassword));
                    adminService.saveAdmin(admin);
                    log.info("Test Admin account seeded successfully.");
                }
            }
        };
    }
}