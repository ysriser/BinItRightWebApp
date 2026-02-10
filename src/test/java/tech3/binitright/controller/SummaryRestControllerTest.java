package tech3.binitright.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.dto.LeaderboardDTO;
import tech3.binitright.dto.UserProfileDTO;
import tech3.binitright.interfacemethods.*;
import tech3.binitright.model.Accessories;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.request.CheckInDataReq;
import tech3.binitright.service.AchievementImplementation;
import tech3.binitright.service.EmissionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SummaryRestControllerTest {
    static class FakeUserService implements UserInterface {
        User userToReturn;
        List<User> byUsername = Collections.emptyList();
        boolean existsByUsername = false;

        @Override
        public User saveUser(User user) { return user; }

        @Override
        public List<User> findByUsername(String username) { return byUsername; }

        @Override
        public User findById(Long userId) { return userToReturn; }

        @Override
        public boolean existsByUsername(String username) { return existsByUsername; }
    }

    static class FakeUserAccessoriesService implements UserAccessoriesInterface {
        List<UserAccessories> rows = Collections.emptyList();

        @Override
        public void save(UserAccessories userAccessories) { }

        @Override
        public List<UserAccessories> findAll() { return Collections.emptyList(); }

        @Override
        public List<UserAccessories> findAllByUser_Id(Long id) { return rows; }

        @Override
        public void equipItem(Long userId, Long accessoriesId) { }

        @Override
        public void unequipItem(Long id, Long accessoriesId) { }
    }

    static class FakeCheckInService implements CheckInInterface {
        Integer total = 0;

        @Override
        public List<CheckIn> getAllCheckIns() { return Collections.emptyList(); }

        @Override
        public CheckIn processCheckIn(CheckInDataReq data, Long userId) throws IOException { return null; }

        @Override
        public List<CheckIn> getPendingCheckIns() { return Collections.emptyList(); }

        @Override
        public Integer getUserTotalRecycled(Long userId) { return total; }

        @Override
        public List<LeaderboardDTO> getMonthlyLeaderboard() { return Collections.emptyList(); }
    }

    // EmissionService has constructor(CheckInRepository) -> call super(null) and override the used method
    static class FakeEmissionService extends EmissionService {
        BigDecimal co2 = null;

        FakeEmissionService() {
            super(null);
        }

        @Override
        public BigDecimal getUserTotalCo2Saved(Long userId) {
            return co2;
        }
    }

    // AchievementImplementation has constructor(AchievementRepository, UserAchievementRepository, UserRepository)
    static class FakeAchievementService extends AchievementImplementation {
        int totalAchievements = 0;

        FakeAchievementService() {
            super(null, null, null);
        }

        @Override
        public int getTotalAchievements(Long userId) {
            return totalAchievements;
        }
    }

    // -------------------- TESTS --------------------

    @Test
    void getProfileSummary_whenAuthNameIsUserId_returnsDto() {
        final SummaryRestController controller = new SummaryRestController();

        final FakeUserService userService = new FakeUserService();
        final FakeUserAccessoriesService userAccessoriesService = new FakeUserAccessoriesService();
        final FakeCheckInService checkInService = new FakeCheckInService();
        final FakeEmissionService emissionService = new FakeEmissionService();
        final FakeAchievementService achievementService = new FakeAchievementService();

        // User data
        User u = new User();
        u.setId(5L);
        u.setName("Sujitha");
        u.setPointBalance(120);
        userService.userToReturn = u;

        // Equipped avatar accessory
        Accessories acc = new Accessories();
        acc.setName("Cool Avatar");
        UserAccessories ua = new UserAccessories();
        ua.setAccessories(acc);
        ua.setEquipped(true);
        userAccessoriesService.rows = List.of(ua);

        // Other service values
        checkInService.total = 12;
        emissionService.co2 = new BigDecimal("3.50");
        achievementService.totalAchievements = 4;

        // Inject
        ReflectionTestUtils.setField(controller, "userService", userService);
        ReflectionTestUtils.setField(controller, "userAccessoriesService", userAccessoriesService);
        ReflectionTestUtils.setField(controller, "checkInService", checkInService);
        ReflectionTestUtils.setField(controller, "emissionService", emissionService);
        ReflectionTestUtils.setField(controller, "achievementService", achievementService);

        Authentication auth = new UsernamePasswordAuthenticationToken("5", null, List.of());

        ResponseEntity<?> response = controller.getProfileSummary(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof UserProfileDTO);

        UserProfileDTO dto = (UserProfileDTO) response.getBody();
        assertEquals("Sujitha", dto.getName());
        assertEquals(120, dto.getPointBalance());
        assertEquals("Cool Avatar", dto.getEquippedAvatarName());
        assertEquals(12, dto.getTotalRecycled());
        assertEquals(4, dto.getTotalAchievements());
        assertEquals(3.50f, dto.getCarbonEmissionSaved(), 0.0001);
    }

    @Test
    void getProfileSummary_whenUserNotFound_returns404() {
        final SummaryRestController controller = new SummaryRestController();

        final FakeUserService userService = new FakeUserService();
        userService.userToReturn = null;

        ReflectionTestUtils.setField(controller, "userService", userService);

        Authentication auth = new UsernamePasswordAuthenticationToken("99", null, List.of());

        ResponseEntity<?> response = controller.getProfileSummary(auth);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void getProfileSummary_whenAuthNameIsUsername_fallsBackToFindByUsername() {
        final SummaryRestController controller = new SummaryRestController();

        final FakeUserService userService = new FakeUserService();
        final FakeUserAccessoriesService userAccessoriesService = new FakeUserAccessoriesService();
        final FakeCheckInService checkInService = new FakeCheckInService();
        final FakeEmissionService emissionService = new FakeEmissionService();
        final FakeAchievementService achievementService = new FakeAchievementService();

        User u = new User();
        u.setId(10L);
        u.setName("User10");
        u.setPointBalance(50);

        userService.byUsername = List.of(u);
        userService.userToReturn = u;

        userAccessoriesService.rows = List.of(); // -> default_avatar
        checkInService.total = 0;
        emissionService.co2 = null; // -> 0f
        achievementService.totalAchievements = 0;

        ReflectionTestUtils.setField(controller, "userService", userService);
        ReflectionTestUtils.setField(controller, "userAccessoriesService", userAccessoriesService);
        ReflectionTestUtils.setField(controller, "checkInService", checkInService);
        ReflectionTestUtils.setField(controller, "emissionService", emissionService);
        ReflectionTestUtils.setField(controller, "achievementService", achievementService);

        Authentication auth = new UsernamePasswordAuthenticationToken("User1", null, List.of());

        ResponseEntity<?> response = controller.getProfileSummary(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        UserProfileDTO dto = (UserProfileDTO) response.getBody();
        assertEquals("default_avatar", dto.getEquippedAvatarName());
        assertEquals(0f, dto.getCarbonEmissionSaved(), 0.0001);
        assertEquals("User10", dto.getName());
        assertEquals(50, dto.getPointBalance());
    }
}