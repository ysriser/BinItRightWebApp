package tech3.binitright.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech3.binitright.interfacemethods.*;
import tech3.binitright.model.*;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.repository.DropOffLocationRepository;
import tech3.binitright.repository.WasteCategoryRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import tech3.binitright.service.AccessoriesImplementation;
import tech3.binitright.service.AdminImplementation;
import tech3.binitright.service.IssueImplementation;
import tech3.binitright.service.UserAccessoriesImplementation;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private IssueInterface issueService;

    @Autowired
    public void setIssueService(IssueImplementation issueImplementation) {
        this.issueService = issueImplementation;
    }

    @Autowired
    private UserAccessoriesInterface userAccessoriesService;

    @Autowired
    public void setUserAccessoriesService(UserAccessoriesImplementation userAccessoriesImplementation) {
        this.userAccessoriesService = userAccessoriesImplementation;
    }

    @Autowired
    private AdminInterface adminService;

    public void setAdminService(AdminImplementation adminserviceImp) {
        this.adminService = adminserviceImp;
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
    @Order(5)
    @Profile({"test", "prod", "default"}) // Avoid running this in "prod" to keep the DB clean
    public CommandLineRunner seedUsers(PasswordEncoder passwordEncoder) {
        return args -> {

            record SeedUser(String username, String email, String name, int accessoriesToAssign) {}

            List<SeedUser> usersToSeed = List.of(
                    new SeedUser("User1", "tester1@binitright.com", "Default Tester 1", 2),
                    new SeedUser("User2", "tester2@binitright.com", "Default Tester 2", 4),
                    new SeedUser("User3", "tester3@binitright.com", "Default Tester 3", 3),
                    new SeedUser("User4", "tester4@binitright.com", "Default Tester 4", 1)
            );

            List<Accessories> availableAccs = accessoriesService.findAll();

            for (SeedUser su : usersToSeed) {

                if (!userService.findByUsername(su.username()).isEmpty()){
                    System.out.println(">>> UserSeeder: " + su.username() + " already exists, skipping.");
                    continue;
                }

                // Create user
                User user = new User();
                user.setUsername(su.username());
                user.setEmailAddress(su.email());
                user.setName(su.name());
                user.setRole("USER");
                user.setPassword_hash(passwordEncoder.encode("password"));

                user.setPointBalance(1000);
                user.setCurrentRank(1);
                user.setCarbonEmissionSaved(0.0f);
                user.setUserAddress("123 Sustainability Lane");

                User savedUser = userService.saveUser(user);

                System.out.println(">>> UserSeeder: Created " + su.username());

                // Assign accessories
                for (int i = 0; i < su.accessoriesToAssign() && i < availableAccs.size(); i++) {
                    Accessories acc = availableAccs.get(i);

                    UserAccessories ua = new UserAccessories();
                    ua.setUser(savedUser);
                    ua.setAccessories(acc);
                    ua.setEquipped(false);

                    userAccessoriesService.save(ua);

                    System.out.println(">>> Assigned " + acc.getName() + " to " + su.username());
                }
            }
        };
    }



    @Bean
    @Order(6)
    @Profile({"test", "prod", "default"})
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

            WasteCategories lighting = new WasteCategories();
            lighting.setName("Lighting");
            lighting.setStreamType(WasteCategories.StreamType.GENERAL);
            lighting.setIsHazardous(false);
            lighting.setIconUrl("lighting");
            lighting.setEmissionFactor(new BigDecimal("2.10"));
            lighting.setAvgWeight(new BigDecimal("0.70"));

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
                    lighting,
                    metal,
                    paper
            ));

            System.out.println(">>> Waste categories seeded (6 types)");
        };
    }

    @Bean
    @Order(7)
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
            c2.setStatus(CheckIn.Status.PROCESSING);

            CheckIn c3 = new CheckIn();
            c3.setUser(user);
            c3.setDropOffLocation(d3);
            c3.setWasteCategories(glass);
            c3.setFileName("glass_1.jpg");
            c3.setQuantity(5);
            c3.setDuration(15L);
            c3.setRewardPoints(25);
            c3.setStatus(CheckIn.Status.PROCESSING);

            checkInRepo.saveAll(List.of(c1, c2, c3));

            System.out.println(">>> Check-ins seeded");

        };
    }

    @Bean
    @Order(8)
    @Profile({"test", "prod", "default"})
    public CommandLineRunner seedIssues() {
        return args -> {
            if (!issueService.findAll().isEmpty()) {
                System.out.println(">>> Issues already exist, skipping.");
                return;
            }

            // Retrieve users seeded in Order 5
            User u1 = userService.findByUsername("User1").getFirst();
            User u2 = userService.findByUsername("User2").getFirst();
            User u3 = userService.findByUsername("User3").getFirst();
            User u4 = userService.findByUsername("User4").getFirst();

            // Assuming at least one admin exists with ID 1
            Admin admin1 = adminService.findById(1L).orElse(null);

            // Issue 1: Login crash
            Issue i1 = new Issue(Issue.IssueCategory.AppProblems, "App crashes immediately after tapping the login button.", Issue.IssueStatus.NEW, u1, null);

            // Issue 2: Overflowing bin
            Issue i2 = new Issue(Issue.IssueCategory.BinIssues, "Recycling bin near Block 512 is overflowing and needs collection.", Issue.IssueStatus.IN_PROGRESS, u2, admin1);

            // Issue 3: Incorrect map location (Resolved)
            // Note: We use the constructor and then manually set dates to match your SQL '2026-02-01' requirement
            Issue i3 = new Issue(Issue.IssueCategory.LocationErrors, "GPS location for Jurong recycling point is incorrect on the map.", Issue.IssueStatus.RESOLVED, u3, admin1);
            i3.setCreatedAt(LocalDateTime.of(2026, 2, 1, 9, 10));
            i3.setResolvedAt(LocalDateTime.of(2026, 2, 3, 15, 25));

            // Issue 4: Slow dashboard
            Issue i4 = new Issue(Issue.IssueCategory.AppProblems, "User dashboard takes more than 10 seconds to load history.", Issue.IssueStatus.NEW, u4, null);

            issueService.saveAll(List.of(i1, i2, i3, i4));
            System.out.println(">>> Issues seeded (4 entries)");
        };
    }
}