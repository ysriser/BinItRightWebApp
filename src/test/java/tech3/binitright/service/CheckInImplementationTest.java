package tech3.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.request.CheckInDataReq;
import tech3.binitright.dto.LeaderboardDTO;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.DropOffLocation;
import tech3.binitright.model.User;
import tech3.binitright.model.WasteCategories;
import tech3.binitright.repository.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckInImplementationTest {
    @Mock private CheckInRepository checkInRepository;
    @Mock private UserRepository userRepository;
    @Mock private LocationRepository locationRepository;
    @Mock private WasteCategoryRepository wasteCatRepository;

    private CheckInImplementation service;
    private FakeAchievementImplementation fakeAchievements;

    // --------- Fake AchievementImplementation (no Mockito, avoids Java25 ByteBuddy issue) ----------
    static class FakeAchievementImplementation extends AchievementImplementation {
        final java.util.List<Long> unlockedIds = new java.util.ArrayList<>();
        boolean profileChecked = false;

        FakeAchievementImplementation() {
            super(null, null, null);
        }

        @Override
        public void unlockAchievement(Long userId, Long achievementId) {
            unlockedIds.add(achievementId);
        }

        @Override
        public void checkProfileAchievements(User user) {
            profileChecked = true;
        }
    }

    @BeforeEach
    void setUp() {
        service = new CheckInImplementation();
        fakeAchievements = new FakeAchievementImplementation();

        // inject mocks + fake into @Autowired fields
        ReflectionTestUtils.setField(service, "checkInRepository", checkInRepository);
        ReflectionTestUtils.setField(service, "userRepository", userRepository);
        ReflectionTestUtils.setField(service, "locationRepository", locationRepository);
        ReflectionTestUtils.setField(service, "wasteCatRepository", wasteCatRepository);
        ReflectionTestUtils.setField(service, "achievementImplementation", fakeAchievements);
    }

    // --------- helpers ----------
    private User user(Long id, Integer balance) {
        User u = new User();
        u.setId(id);
        u.setPointBalance(balance);
        return u;
    }

    private DropOffLocation location(String id) {
        DropOffLocation l = new DropOffLocation();
        l.setId(id);
        return l;
    }

    private WasteCategories category(String name) {
        WasteCategories c = new WasteCategories();
        // if your entity doesn't have setName, remove this line
        c.setName(name);
        return c;
    }

    private CheckInDataReq req(String binId, String wasteCategory, int qty, LocalDateTime time) {
        CheckInDataReq r = new CheckInDataReq();
        r.setBinId(binId);
        r.setWasteCategory(wasteCategory);
        r.setQuantity(qty);
        r.setDuration(10L);
        r.setCheckInTime(time);
        r.setVideoKey("video.mp4");
        return r;
    }

    // -------------------- TESTS --------------------

    @Test
    void getAllCheckIns_returnsRepoResult() {
        when(checkInRepository.findAllWithDetails()).thenReturn(List.of(new CheckIn(), new CheckIn()));

        List<CheckIn> result = service.getAllCheckIns();

        assertEquals(2, result.size());
        verify(checkInRepository).findAllWithDetails();
    }

    @Test
    void processCheckIn_qtyLE10_setsApproved_rewardPoints_updatesUserBalance_unlocksAchievements() throws Exception {
        // given
        User u = user(10L, 100);
        DropOffLocation loc = location("BIN-1");
        WasteCategories cat = category("Plastic");

        // time 06:30 => early bird => achievement 7
        CheckInDataReq data = req("BIN-1", "Plastic", 5, LocalDateTime.of(2026, 2, 10, 6, 30));

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));
        when(locationRepository.findById("BIN-1")).thenReturn(Optional.of(loc));
        when(wasteCatRepository.findByNameIgnoreCase("Plastic")).thenReturn(Optional.of(cat));

        when(checkInRepository.save(any(CheckIn.class))).thenAnswer(inv -> inv.getArgument(0));

        // total checkins = 10 => unlock 1 and 2
        when(checkInRepository.countByUser(u)).thenReturn(10L);

        // when
        CheckIn saved = service.processCheckIn(data, 10L);

        // then
        assertNotNull(saved);
        assertEquals(CheckIn.Status.APPROVED, saved.getStatus());
        assertEquals(50, saved.getRewardPoints()); // 5 * 10

        // user balance updated and saved
        assertEquals(150, u.getPointBalance());
        verify(userRepository).save(u);

        // achievements unlocked
        assertTrue(fakeAchievements.unlockedIds.contains(1L), "should unlock achievement 1");
        assertTrue(fakeAchievements.unlockedIds.contains(2L), "should unlock achievement 2");
        assertTrue(fakeAchievements.unlockedIds.contains(7L), "should unlock early bird achievement 7");

        // profile achievements checked
        assertTrue(fakeAchievements.profileChecked, "checkProfileAchievements should be called");
    }

    @Test
    void processCheckIn_qtyGT10_setsProcessing_noRewardPoints_noUserSave_unlocksAchievements() throws Exception {
        // given
        User u = user(10L, 100);
        DropOffLocation loc = location("BIN-1");
        WasteCategories cat = category("Plastic");

        // time 23:10 => night owl => achievement 8
        CheckInDataReq data = req("BIN-1", "Plastic", 20, LocalDateTime.of(2026, 2, 10, 23, 10));

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));
        when(locationRepository.findById("BIN-1")).thenReturn(Optional.of(loc));
        when(wasteCatRepository.findByNameIgnoreCase("Plastic")).thenReturn(Optional.of(cat));

        when(checkInRepository.save(any(CheckIn.class))).thenAnswer(inv -> inv.getArgument(0));

        // total checkins = 1 => unlock 1 only
        when(checkInRepository.countByUser(u)).thenReturn(1L);

        // when
        CheckIn saved = service.processCheckIn(data, 10L);

        // then
        assertEquals(CheckIn.Status.PROCESSING, saved.getStatus());
        assertNull(saved.getRewardPoints(), "rewardPoints stays null for qty>10 in current code");

        // no user save when no rewardPoints
        verify(userRepository, never()).save(any(User.class));
        assertEquals(100, u.getPointBalance());

        // achievements
        assertTrue(fakeAchievements.unlockedIds.contains(1L));
        assertTrue(fakeAchievements.unlockedIds.contains(8L));
        assertTrue(fakeAchievements.profileChecked);
    }

    @Test
    void processCheckIn_userNotFound_throws() {
        CheckInDataReq data = req("BIN-1", "Plastic", 5, LocalDateTime.now());
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.processCheckIn(data, 99L));

        assertTrue(ex.getMessage().contains("User not found"));
        verify(checkInRepository, never()).save(any());
    }

    @Test
    void processCheckIn_locationNotFound_throws() {
        User u = user(10L, 0);
        CheckInDataReq data = req("BIN-X", "Plastic", 5, LocalDateTime.now());

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));
        when(locationRepository.findById("BIN-X")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.processCheckIn(data, 10L));

        assertTrue(ex.getMessage().contains("Location not found"));
        verify(checkInRepository, never()).save(any());
    }

    @Test
    void processCheckIn_categoryNotFound_throws() {
        User u = user(10L, 0);
        DropOffLocation loc = location("BIN-1");
        CheckInDataReq data = req("BIN-1", "Unknown", 5, LocalDateTime.now());

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));
        when(locationRepository.findById("BIN-1")).thenReturn(Optional.of(loc));
        when(wasteCatRepository.findByNameIgnoreCase("Unknown")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.processCheckIn(data, 10L));

        assertTrue(ex.getMessage().contains("Waste category not found"));
        verify(checkInRepository, never()).save(any());
    }

    @Test
    void getPendingCheckIns_returnsRepoResult() {
        when(checkInRepository.findPendingWithDetails()).thenReturn(List.of(new CheckIn()));

        List<CheckIn> result = service.getPendingCheckIns();

        assertEquals(1, result.size());
        verify(checkInRepository).findPendingWithDetails();
    }

    @Test
    void getUserTotalRecycled_returnsRepoValue() {
        when(checkInRepository.getTotalRecycledByUser(10L)).thenReturn(42);

        Integer total = service.getUserTotalRecycled(10L);

        assertEquals(42, total);
        verify(checkInRepository).getTotalRecycledByUser(10L);
    }

    @Test
    void getMonthlyLeaderboard_callsRepoWithStartOfMonthAndLimit5() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        PageRequest page = PageRequest.of(0, 5);

        when(checkInRepository.findTopRecyclers(eq(startOfMonth), eq(page)))
                .thenReturn(Collections.emptyList());

        List<LeaderboardDTO> result = service.getMonthlyLeaderboard();

        assertNotNull(result);
        verify(checkInRepository).findTopRecyclers(eq(startOfMonth), eq(page));
    }
}