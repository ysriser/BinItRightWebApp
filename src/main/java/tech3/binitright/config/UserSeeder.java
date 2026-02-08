package tech3.binitright.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import tech3.binitright.interfacemethods.AccessoriesInterface;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Accessories;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.model.WasteCategories;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.repository.DropOffLocationRepository;
import tech3.binitright.repository.WasteCategoryRepository;
import tech3.binitright.service.AccessoriesImplementation;
import tech3.binitright.service.AdminImplementation;
import tech3.binitright.service.IssueImplementation;
import tech3.binitright.service.UserAccessoriesImplementation;

@Configuration
public final class UserSeeder {

    private final UserInterface userService;
    private final WasteCategoryRepository wasteRepo;
    private final DropOffLocationRepository dropOffRepo;
    private final CheckInRepository checkInRepo;

    @Autowired
    private AccessoriesInterface accessoriesService;

    @Autowired
    public void setAccessoriesService(final AccessoriesImplementation accessoriesImplementation) {
        this.accessoriesService = accessoriesImplementation;
    }

    @Autowired
    private IssueInterface issueService;

    @Autowired
    public void setIssueService(final IssueImplementation issueImplementation) {
        this.issueService = issueImplementation;
    }

    @Autowired
    private UserAccessoriesInterface userAccessoriesService;

    @Autowired
    public void setUserAccessoriesService(final UserAccessoriesImplementation userAccessoriesImplementation) {
        this.userAccessoriesService = userAccessoriesImplementation;
    }

    @Autowired
    private AdminInterface adminService;

    public void setAdminService(final AdminImplementation adminserviceImp) {
        this.adminService = adminserviceImp;
    }

    public UserSeeder(final UserInterface userService,
                      final WasteCategoryRepository wasteRepo,
                      final DropOffLocationRepository dropOffRepo,
                      final CheckInRepository checkInRepo) {
        this.userService = userService;
        this.wasteRepo = wasteRepo;
        this.dropOffRepo = dropOffRepo;
        this.checkInRepo = checkInRepo;
    }

    @Bean
    @Order(5)
    @Profile({"test", "prod", "default"})
    public CommandLineRunner seedUsers(final PasswordEncoder passwordEncoder) {
        return args -> {
            record SeedUser(String username, String email, String name, int accessoriesToAssign) {}
            final List<SeedUser> usersToSeed = List.of(
                    new SeedUser("User1", "tester1@binitright.com", "Default Tester 1", 2),
                    new SeedUser("User2", "tester2@binitright.com", "Default Tester 2", 4),
                    new SeedUser("User3", "tester3@binitright.com", "Default Tester 3", 3),
                    new SeedUser("User4", "tester4@binitright.com", "Default Tester 4", 1)
            );

            final List<Accessories> availableAccs = accessoriesService.findAll();
            for (final SeedUser su : usersToSeed) {
                if (!userService.findByUsername(su.username()).isEmpty()) {
                    continue;
                }
                final User user = new User();
                user.setUsername(su.username());
                user.setEmailAddress(su.email());
                user.setName(su.name());
                user.setRole("USER");
                user.setPasswordHash(passwordEncoder.encode("password"));
                user.setPointBalance(1000);
                user.setCurrentRank(1);
                user.setCarbonEmissionSaved(0.0f);
                user.setUserAddress("123 Sustainability Lane");
                final User savedUser = userService.saveUser(user);

                for (int i = 0; i < su.accessoriesToAssign() && i < availableAccs.size(); i++) {
                    final Accessories acc = availableAccs.get(i);
                    final UserAccessories ua = new UserAccessories();
                    ua.setUser(savedUser);
                    ua.setAccessories(acc);
                    ua.setEquipped(false);
                    userAccessoriesService.save(ua);
                }
            }
        };
    }

    @Bean
    @Order(6)
    @Profile({"test", "prod", "default"})
    public CommandLineRunner seedWasteCategories() {
        return args -> {
            if (wasteRepo.count() > 0) {
                return;
            }
            final WasteCategories plastic = new WasteCategories();
            plastic.setName("Plastic");
            plastic.setStreamType(WasteCategories.StreamType.GENERAL);
            plastic.setIsHazardous(false);
            plastic.setIconUrl("plastic");
            plastic.setEmissionFactor(new BigDecimal("1.50"));
            plastic.setAvgWeight(new BigDecimal("0.30"));

            final WasteCategories ewaste = new WasteCategories();
            ewaste.setName("E-Waste");
            ewaste.setStreamType(WasteCategories.StreamType.EUWASTE);
            ewaste.setIsHazardous(true);
            ewaste.setIconUrl("e-waste");
            ewaste.setEmissionFactor(new BigDecimal("4.20"));
            ewaste.setAvgWeight(new BigDecimal("1.20"));

            wasteRepo.saveAll(List.of(plastic, ewaste));
        };
    }
}