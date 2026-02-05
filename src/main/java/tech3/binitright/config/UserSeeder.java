package tech3.binitright.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.DropOffLocation;
import tech3.binitright.model.User;
import tech3.binitright.model.WasteCategories;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.repository.DropOffLocationRepository;
import tech3.binitright.repository.WasteCategoryRepository;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class UserSeeder {
    private final UserInterface userService;
    private final WasteCategoryRepository wasteRepo;
    private final DropOffLocationRepository dropOffRepo;
    private final CheckInRepository checkInRepo;

    public UserSeeder(UserInterface userService,
                      WasteCategoryRepository wasteRepo,
                      DropOffLocationRepository dropOffRepo,
                      CheckInRepository checkInRepo) {
        this.userService = userService;
        this.wasteRepo = wasteRepo;
        this.dropOffRepo = dropOffRepo;
        this.checkInRepo = checkInRepo;
    }

    private void seedUser(PasswordEncoder passwordEncoder,
                          String username,
                          String email,
                          String name,
                          String role) {

        if (!userService.findByUsername(username).isEmpty())
        {
            System.out.println(">>> User '" + username + "' already exists. Skipping.");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmailAddress(email);
        user.setName(name);
        user.setRole(role);

        // default dev password
        user.setPassword_hash(passwordEncoder.encode("password"));

        userService.saveUser(user);

        System.out.println(">>> Seeded user: " + username);
    }


    @Bean
    @Profile({"test", "default", "dev"}) // Avoid running this in "prod"
    public CommandLineRunner seedUsers(PasswordEncoder passwordEncoder) {
        return args -> {

            seedUser(passwordEncoder, "User1", "user1@test.com", "User One", "USER");
            seedUser(passwordEncoder, "User2", "user2@test.com", "User Two", "USER");
            seedUser(passwordEncoder, "User3", "user3@test.com", "User Three", "USER");
            seedUser(passwordEncoder, "User4", "user4@test.com", "User Four", "USER");

            System.out.println(">>> User seeding completed.");
        };
    }

    @Bean
    @Profile({"test", "default", "dev"})
    public CommandLineRunner seedWasteCategories() {
        return args -> {

            // Skip if already seeded
            if (wasteRepo.count() > 0) {
                System.out.println(">>> Waste categories already exist, skipping");
                return;
            }

            WasteCategories plastic = new WasteCategories();
            plastic.setName("Plastic");
            plastic.setStreamType(WasteCategories.StreamType.GENERAL);
            plastic.setIsHazardous(false);
            plastic.setIconUrl("plastic");
            plastic.setEmissionFactor(new BigDecimal("1.50"));
            plastic.setAvgWeight(new BigDecimal("0.30"));

            WasteCategories ewaste = new WasteCategories();
            ewaste.setName("E-Waste");
            ewaste.setStreamType(WasteCategories.StreamType.E_WASTE);
            ewaste.setIsHazardous(true);
            ewaste.setIconUrl("e-waste");
            ewaste.setEmissionFactor(new BigDecimal("4.20"));
            ewaste.setAvgWeight(new BigDecimal("1.20"));

            WasteCategories glass = new WasteCategories();
            glass.setName("Glass");
            glass.setStreamType(WasteCategories.StreamType.GENERAL);
            glass.setIsHazardous(false);
            glass.setIconUrl("glass");
            glass.setEmissionFactor(new BigDecimal("0.90"));
            glass.setAvgWeight(new BigDecimal("0.50"));

            WasteCategories textile = new WasteCategories();
            textile.setName("Textile");
            textile.setStreamType(WasteCategories.StreamType.GENERAL);
            textile.setIsHazardous(false);
            textile.setIconUrl("textile");
            textile.setEmissionFactor(new BigDecimal("2.10"));
            textile.setAvgWeight(new BigDecimal("0.70"));

            WasteCategories metal = new WasteCategories();
            metal.setName("Metal");
            metal.setStreamType(WasteCategories.StreamType.GENERAL);
            metal.setIsHazardous(false);
            metal.setIconUrl("metal");
            metal.setEmissionFactor(new BigDecimal("3.00"));
            metal.setAvgWeight(new BigDecimal("0.80"));

            WasteCategories paper = new WasteCategories();
            paper.setName("Paper");
            paper.setStreamType(WasteCategories.StreamType.GENERAL);
            paper.setIsHazardous(false);
            paper.setIconUrl("paper");
            paper.setEmissionFactor(new BigDecimal("1.10"));
            paper.setAvgWeight(new BigDecimal("0.25"));

            wasteRepo.saveAll(List.of(
                    plastic,
                    ewaste,
                    glass,
                    textile,
                    metal,
                    paper
            ));

            System.out.println(">>> Waste categories seeded (6 types)");
        };
    }


}

