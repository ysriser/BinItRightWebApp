package techthree.binitright.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import techthree.binitright.dto.UserProfileDTO;
import techthree.binitright.interfacemethods.*;
import techthree.binitright.model.Accessories;
import techthree.binitright.model.CheckIn;
import techthree.binitright.model.User;
import techthree.binitright.model.UserAccessories;
import techthree.binitright.request.CheckInDataReq;
import techthree.binitright.service.AchievementImplementation;
import techthree.binitright.service.EmissionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SummaryRestControllerTest {

    // ---------- Fake Services ----------

    static class FakeUserService implements UserInterface {
        User userToReturn;
        List<User> byUsername = Collections.emptyList();

        @Override public User saveUser(User user) { return user; }
        @Override public List<User> findByUsername(String username) { return byUsername; }
        @Override public User findById(Long userId) { return userToReturn; }
        @Override public boolean existsByUsername(String username) { return false; }
        @Override public boolean existsByEmailAddress(String emailAddress) { return false; }
    }

    static class FakeUserAccessoriesService implements UserAccessoriesInterface {
        List<UserAccessories> rows = Collections.emptyList();

        @Override public void save(UserAccessories ua) {}
        @Override public List<UserAccessories> findAll() { return Collections.emptyList(); }
        @Override public List<UserAccessories> findAllByUser_Id(Long id) { return rows; }
        @Override public void equipItem(Long userId, Long accessoriesId) {}
        @Override public void unequipItem(Long id, Long accessoriesId) {}
    }

    static class FakeCheckInService implements CheckInInterface {
        Integer total = 0;

        @Override public Integer getUserTotalRecycled(Long userId) { return total; }
        @Override public List getAllCheckIns() { return Collections.emptyList(); }

        @Override
        public CheckIn processCheckIn(CheckInDataReq data, Long userId) throws IOException {
            return null;
        }

        @Override public List getPendingCheckIns() { return Collections.emptyList(); }
        @Override public List getMonthlyLeaderboard() { return Collections.emptyList(); }
    }

    static class FakeEmissionService extends EmissionService {
        BigDecimal co2 = null;
        FakeEmissionService() { super(null); }
        @Override public BigDecimal getUserTotalCo2Saved(Long userId) { return co2; }
    }

    static class FakeAchievementService extends AchievementImplementation {
        int totalAchievements = 0;
        FakeAchievementService() { super(null, null, null); }
        @Override public int getTotalAchievements(Long userId) { return totalAchievements; }
    }

    static class FakeChatService implements ChatInterface {

        @Override
        public String askRecyclingAssistant(String userMessage) {
            return "assistant";
        }

        @Override
        public String generateProgressSummary(
                int pointBalance,
                double carbonEmissionSaved,
                int currentRank,
                int totalRecycledItems
        ) {
            return "summary";
        }
    }

    // ---------- Helper to build controller ----------

    private SummaryRestController buildController(
            FakeUserService userService,
            FakeUserAccessoriesService accessoriesService,
            FakeCheckInService checkInService,
            FakeEmissionService emissionService,
            FakeAchievementService achievementService,
            FakeChatService chatService
    ) {
        SummaryRestController controller = new SummaryRestController();

        ReflectionTestUtils.setField(controller, "userService", userService);
        ReflectionTestUtils.setField(controller, "userAccessoriesService", accessoriesService);
        ReflectionTestUtils.setField(controller, "checkInService", checkInService);
        ReflectionTestUtils.setField(controller, "emissionService", emissionService);
        ReflectionTestUtils.setField(controller, "achievementService", achievementService);
        ReflectionTestUtils.setField(controller, "chatService", chatService);

        return controller;
    }

    // ---------- TEST 1 ----------

    @Test
    void getProfileSummary_whenAuthNameIsUserId_returnsDto() {

        FakeUserService userService = new FakeUserService();
        FakeUserAccessoriesService accessoriesService = new FakeUserAccessoriesService();
        FakeCheckInService checkInService = new FakeCheckInService();
        FakeEmissionService emissionService = new FakeEmissionService();
        FakeAchievementService achievementService = new FakeAchievementService();
        FakeChatService chatService = new FakeChatService();

        // User setup
        User u = new User();
        u.setId(5L);
        u.setName("John");
        u.setPointBalance(120);
        u.setCurrentRank(1);
        u.setCarbonEmissionSaved(0.0F);
        userService.userToReturn = u;

        // Equipped avatar
        Accessories acc = new Accessories();
        acc.setName("Cool Avatar");
        UserAccessories ua = new UserAccessories();
        ua.setAccessories(acc);
        ua.setEquipped(true);
        accessoriesService.rows = List.of(ua);

        checkInService.total = 12;
        emissionService.co2 = new BigDecimal("3.50");
        achievementService.totalAchievements = 4;

        SummaryRestController controller = buildController(
                userService, accessoriesService, checkInService,
                emissionService, achievementService, chatService
        );

        Authentication auth = new UsernamePasswordAuthenticationToken("5", null);

        ResponseEntity<UserProfileDTO> response = controller.getProfileSummary(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        UserProfileDTO dto = response.getBody();
        assertEquals("John", dto.getName());
        assertEquals(120, dto.getPointBalance());
        assertEquals("Cool Avatar", dto.getEquippedAvatarName());
        assertEquals(12, dto.getTotalRecycled());
        assertEquals(4, dto.getTotalAchievements());
        assertEquals(3.50f, dto.getCarbonEmissionSaved(), 0.0001);
    }

    // ---------- TEST 2 ----------

    @Test
    void getProfileSummary_whenUserNotFound_returns404() {

        FakeUserService userService = new FakeUserService();

        SummaryRestController controller = buildController(
                userService,
                new FakeUserAccessoriesService(),
                new FakeCheckInService(),
                new FakeEmissionService(),
                new FakeAchievementService(),
                new FakeChatService()
        );

        Authentication auth = new UsernamePasswordAuthenticationToken("99", null);

        ResponseEntity<UserProfileDTO> response = controller.getProfileSummary(auth);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ---------- TEST 3 ----------

    @Test
    void getProfileSummary_whenAuthNameIsUsername_fallsBackToFindByUsername() {

        FakeUserService userService = new FakeUserService();
        FakeUserAccessoriesService accessoriesService = new FakeUserAccessoriesService();
        FakeCheckInService checkInService = new FakeCheckInService();
        FakeEmissionService emissionService = new FakeEmissionService();
        FakeAchievementService achievementService = new FakeAchievementService();
        FakeChatService chatService = new FakeChatService();

        User u = new User();
        u.setId(10L);
        u.setName("User10");
        u.setPointBalance(50);
        u.setCurrentRank(1);
        u.setCarbonEmissionSaved(0.0F);

        userService.byUsername = List.of(u);
        userService.userToReturn = u;

        accessoriesService.rows = List.of(); // default avatar

        SummaryRestController controller = buildController(
                userService, accessoriesService, checkInService,
                emissionService, achievementService, chatService
        );

        Authentication auth = new UsernamePasswordAuthenticationToken("User1", null);

        ResponseEntity<UserProfileDTO> response = controller.getProfileSummary(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        UserProfileDTO dto = response.getBody();
        assertEquals("default_avatar", dto.getEquippedAvatarName());
        assertEquals("User10", dto.getName());
        assertEquals(50, dto.getPointBalance());
    }
}
