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

    @Bean
    @Profile({"test", "default"})
    public CommandLineRunner seedWasteCategories() {
        return args -> {

            // Seed waste categories (if missing)
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

            wasteRepo.saveAll(List.of(plastic, ewaste, glass));

            System.out.println(">>> Waste categories seeded");

        };
    }

   @Bean
    @Profile({"test", "default"})
    public CommandLineRunner seedCheckIns() {
        return args -> {

            // Ensure user exists
            User user = userService.findByUsername("User1")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User1 must exist before seeding CheckIns"));

            if (checkInRepo.count() > 0) {
                System.out.println(">>> Check-ins already exist, skipping");
                return;
            }

            WasteCategories plastic = wasteRepo.findByNameIgnoreCase("Plastic").orElseThrow(() -> new RuntimeException("Plastic category missing"));
            WasteCategories ewaste = wasteRepo.findByNameIgnoreCase("E-Waste").orElseThrow(() -> new RuntimeException("E-Waste category missing"));
            WasteCategories glass = wasteRepo.findByNameIgnoreCase("Glass").orElseThrow(() -> new RuntimeException("Glass category missing"));

            DropOffLocation d1 = dropOffRepo.findById("06383D31CA5CC778").orElseThrow();
            DropOffLocation d2 = dropOffRepo.findById("06193A57B84223C5").orElseThrow();
            DropOffLocation d3 = dropOffRepo.findById("2485B751C8B77474").orElseThrow();

            CheckIn c1 = new CheckIn();
            c1.setUser(user);
            c1.setDropOffLocation(d1);
            c1.setWasteCategories(plastic);
            c1.setFileName("plastic_1.jpg");
            c1.setQuantity(3);
            c1.setDuration(12L);
            c1.setRewardPoints(30);
            c1.setStatus(CheckIn.Status.APPROVED);

            CheckIn c2 = new CheckIn();
            c2.setUser(user);
            c2.setDropOffLocation(d2);
            c2.setWasteCategories(ewaste);
            c2.setFileName("ewaste_1.jpg");
            c2.setQuantity(1);
            c2.setDuration(20L);
            c2.setRewardPoints(50);
            c2.setStatus(CheckIn.Status.APPROVED);

            CheckIn c3 = new CheckIn();
            c3.setUser(user);
            c3.setDropOffLocation(d3);
            c3.setWasteCategories(glass);
            c3.setFileName("glass_1.jpg");
            c3.setQuantity(5);
            c3.setDuration(15L);
            c3.setRewardPoints(25);
            c3.setStatus(CheckIn.Status.APPROVED);

            checkInRepo.saveAll(List.of(c1, c2, c3));

            System.out.println(">>> Check-ins seeded");

        };
    }
}