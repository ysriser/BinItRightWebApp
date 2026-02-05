package tech3.binitright.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech3.binitright.interfacemethods.AccessoriesInterface;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Accessories;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.service.AccessoriesImplementation;
import tech3.binitright.service.UserAccessoriesImplementation;

import java.util.List;

@Configuration
public class UserSeeder {

    private final UserInterface userService;

    @Autowired
    private AccessoriesInterface accessoriesService;

    @Autowired
    public void setAccessoriesService(AccessoriesImplementation accessoriesImplementation) {
        this.accessoriesService = accessoriesImplementation;
    }

    @Autowired
    private UserAccessoriesInterface userAccessoriesService;

    @Autowired
    public void setUserAccessoriesService(UserAccessoriesImplementation userAccessoriesImplementation) {
        this.userAccessoriesService = userAccessoriesImplementation;
    }

    public UserSeeder(UserInterface userService) {
        this.userService = userService;
    }

    @Bean
    @Profile({"test", "prod", "default"}) // Avoid running this in "prod" to keep the DB clean
    public CommandLineRunner seedUsers(PasswordEncoder passwordEncoder) {
        return args -> {
            String testUsername = "User1";

            // Check if user already exists
            if (userService.findByUsername(testUsername).isEmpty()) {

                // Create and configure the User
                User user = new User();
                user.setUsername(testUsername);
                user.setEmailAddress("tester@binitright.com");
                user.setName("Default Tester");
                user.setRole("USER");
                user.setPassword_hash(passwordEncoder.encode("password"));

                // Set requested stats
                user.setPointBalance(1000); // Assuming this is in BinItRightUser
                user.setCurrentRank(1);
                user.setCarbonEmissionSaved(0.0f);
                user.setUserAddress("123 Sustainability Lane");

                // Save user to generate the ID
                User savedUser = userService.saveUser(user);
                System.out.println(">>> UserSeeder: Created user '" + testUsername + "' with 1000 points.");

                // Fetch the accessories we added in data.sql
                List<Accessories> availableAccs = accessoriesService.findAll();

                // Link the 3 accessories to the user
                // We iterate through what we found in the DB (Dress, Suit, Sports Attire)
                for (int i = 0; i < 2 && i < availableAccs.size(); i++) {
                    Accessories acc = availableAccs.get(i);
                    UserAccessories ua = new UserAccessories();
                    ua.setUser(savedUser);
                    ua.setAccessories(acc);
                    ua.setEquipped(false);
                    userAccessoriesService.save(ua);
                    System.out.println(">>> UserSeeder: Assigned " + acc.getName() + " to " + testUsername);
                }
            } else {
                System.out.println(">>> UserSeeder: Test user already exists, skipping seeding.");
            }

            String testUsername2 = "User2";

            // Check if user already exists
            if (userService.findByUsername(testUsername2).isEmpty()) {

                // Create and configure the User
                User user = new User();
                user.setUsername(testUsername2);
                user.setEmailAddress("tester2@binitright.com");
                user.setName("Default Tester 2");
                user.setRole("USER");
                user.setPassword_hash(passwordEncoder.encode("password"));

                // Set requested stats
                user.setPointBalance(1000); // Assuming this is in BinItRightUser
                user.setCurrentRank(1);
                user.setCarbonEmissionSaved(0.0f);
                user.setUserAddress("123 Sustainability Lane");

                // Save user to generate the ID
                User savedUser = userService.saveUser(user);
                System.out.println(">>> UserSeeder: Created user '" + testUsername2 + "' with 1000 points.");

                // Fetch the accessories we added in data.sql
                List<Accessories> availableAccs = accessoriesService.findAll();

                // Link the 3 accessories to the user
                // We iterate through what we found in the DB (Dress, Suit, Sports Attire)
                for (int i = 0; i < 4 && i < availableAccs.size(); i++) {
                    Accessories acc = availableAccs.get(i);
                    UserAccessories ua = new UserAccessories();
                    ua.setUser(savedUser);
                    ua.setAccessories(acc);
                    ua.setEquipped(false);
                    userAccessoriesService.save(ua);
                    System.out.println(">>> UserSeeder: Assigned " + acc.getName() + " to " + testUsername);
                }
            } else {
                System.out.println(">>> UserSeeder: Test user 2 already exists, skipping seeding.");
            }
        };
    }
}

