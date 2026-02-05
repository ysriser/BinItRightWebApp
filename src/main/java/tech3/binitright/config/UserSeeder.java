package tech3.binitright.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech3.binitright.interfacemethods.AccessoriesInterface;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.DropOffLocation;
import tech3.binitright.model.Accessories;
import tech3.binitright.model.User;
import tech3.binitright.model.WasteCategories;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.repository.DropOffLocationRepository;
import tech3.binitright.repository.WasteCategoryRepository;

import java.math.BigDecimal;
import java.util.List;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.service.AccessoriesImplementation;
import tech3.binitright.service.UserAccessoriesImplementation;

import java.util.List;

@Configuration
public class UserSeeder {

    private final UserInterface userService;
    private final WasteCategoryRepository wasteRepo;
    private final DropOffLocationRepository dropOffRepo;
    private final CheckInRepository checkInRepo;

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
    @Order(4)
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
    @Bean
    @Profile({"test", "default", "dev"})
    public CommandLineRunner seedWasteCategories() {
        return args -> {

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

    @Bean
    @Order(6)
    @Profile({"test", "prod", "default"})
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