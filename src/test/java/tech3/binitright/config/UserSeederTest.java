package tech3.binitright.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.interfacemethods.*;
import tech3.binitright.model.*;
import tech3.binitright.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserSeederTest {

    private UserSeeder userSeeder;

    private UserInterface userService;
    private WasteCategoryRepository wasteRepo;
    private DropOffLocationRepository dropOffRepo;
    private CheckInRepository checkInRepo;

    private AccessoriesInterface accessoriesService;
    private UserAccessoriesInterface userAccessoriesService;
    private IssueInterface issueService;
    private AdminInterface adminService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        userService = Mockito.mock(UserInterface.class);
        wasteRepo = Mockito.mock(WasteCategoryRepository.class);
        dropOffRepo = Mockito.mock(DropOffLocationRepository.class);
        checkInRepo = Mockito.mock(CheckInRepository.class);

        userSeeder = new UserSeeder(userService, wasteRepo, dropOffRepo, checkInRepo);

        accessoriesService = Mockito.mock(AccessoriesInterface.class);
        userAccessoriesService = Mockito.mock(UserAccessoriesInterface.class);
        issueService = Mockito.mock(IssueInterface.class);
        adminService = Mockito.mock(AdminInterface.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        ReflectionTestUtils.setField(userSeeder, "accessoriesService", accessoriesService);
        ReflectionTestUtils.setField(userSeeder, "userAccessoriesService", userAccessoriesService);
        ReflectionTestUtils.setField(userSeeder, "issueService", issueService);
        ReflectionTestUtils.setField(userSeeder, "adminService", adminService);
    }

    @Test
    void seedUsers_WhenUsersDoNotExist_ShouldCreateUsersAndAssignAccessories() throws Exception {

        when(userService.findByUsername(anyString())).thenReturn(Collections.emptyList());
        when(accessoriesService.findAll()).thenReturn(List.of(new Accessories()));
        when(userService.saveUser(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        CommandLineRunner runner = userSeeder.seedUsers(passwordEncoder);
        runner.run();

        verify(userService, atLeast(4)).saveUser(any(User.class));
        verify(userAccessoriesService, atLeastOnce()).save(any(UserAccessories.class));
    }

    @Test
    void seedWasteCategories_WhenEmpty_ShouldSaveSixCategories() throws Exception {

        when(wasteRepo.count()).thenReturn(0L);
        CommandLineRunner runner = userSeeder.seedWasteCategories();
        runner.run();
        verify(wasteRepo, times(1)).saveAll(anyList());
    }

    @Test
    void seedCheckIns_WhenUserExists_ShouldSaveCheckIns() throws Exception {

        User mockUser = new User();
        when(userService.findByUsername("User1")).thenReturn(List.of(mockUser));
        when(checkInRepo.count()).thenReturn(0L);
        when(wasteRepo.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(new WasteCategories()));
        when(dropOffRepo.findById(anyString())).thenReturn(Optional.of(new DropOffLocation()));
        CommandLineRunner runner = userSeeder.seedCheckIns();
        runner.run();
        verify(checkInRepo, times(1)).saveAll(anyList());
    }

    @Test
    void seedIssues_WhenNoneExist_ShouldSaveFourIssues() throws Exception {

        when(issueService.findAll()).thenReturn(Collections.emptyList());
        when(userService.findByUsername(anyString())).thenReturn(List.of(new User()));
        when(adminService.findById(anyLong())).thenReturn(Optional.of(new Admin()));
        CommandLineRunner runner = userSeeder.seedIssues();
        runner.run();
        verify(issueService, times(1)).saveAll(anyList());
    }
}
