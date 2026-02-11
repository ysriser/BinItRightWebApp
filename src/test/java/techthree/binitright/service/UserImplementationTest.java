package techthree.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import techthree.binitright.model.Achievement;
import techthree.binitright.model.User;
import techthree.binitright.model.UserAchievement;
import techthree.binitright.repository.AchievementRepository;
import techthree.binitright.repository.UserAchievementRepository;
import techthree.binitright.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserImplementationTest {
    @Mock private UserRepository userRepo;

    // Dependencies to build REAL AchievementImplementation
    @Mock private AchievementRepository achievementRepo;
    @Mock private UserAchievementRepository userAchievementRepo;
    @Mock private UserRepository achievementUserRepo; // used by AchievementImplementation

    private AchievementImplementation achievementImplementation; // REAL
    private UserImplementation service;

    @BeforeEach
    void setUp() {
        achievementImplementation =
                new AchievementImplementation(achievementRepo, userAchievementRepo, achievementUserRepo);

        service = new UserImplementation();

        // field injection (@Autowired) -> set manually
        try {
            var f1 = UserImplementation.class.getDeclaredField("userRepo");
            f1.setAccessible(true);
            f1.set(service, userRepo);

            var f2 = UserImplementation.class.getDeclaredField("achievementImplementation");
            f2.setAccessible(true);
            f2.set(service, achievementImplementation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // -------- helpers --------
    private User user(Long id, Integer balance, int rank) {
        User u = new User();
        u.setId(id);
        u.setPointBalance(balance);
        u.setCurrentRank(rank);
        return u;
    }

    private Achievement achievement(Long id) {
        Achievement a = new Achievement();
        a.setAchievementId(id);
        a.setName("dummy");
        return a;
    }

    @Test
    void saveUser_savesUser_andCallsCheckProfileAchievements() {
        // checkProfileAchievements(user) may call unlockAchievement(5L/6L)
        // so we stub the repos it needs so it won’t throw.

        User u = user(1L, 6000, 3); // triggers both 5L and 6L

        when(userRepo.save(u)).thenReturn(u);

        // unlockAchievement uses: userAchievementRepo.findByUser_Id, achievementUserRepo.findById, achievementRepo.findById, userAchievementRepo.save
        when(userAchievementRepo.findByUser_Id(1L)).thenReturn(List.of());
        when(achievementUserRepo.findById(1L)).thenReturn(Optional.of(u));
        when(achievementRepo.findById(5L)).thenReturn(Optional.of(achievement(5L)));
        when(achievementRepo.findById(6L)).thenReturn(Optional.of(achievement(6L)));
        when(userAchievementRepo.save(any(UserAchievement.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = service.saveUser(u);

        assertSame(u, saved);

        verify(userRepo).save(u);

        // verify that checkProfileAchievements resulted in at least one save(UserAchievement)
        verify(userAchievementRepo, atLeastOnce()).save(any(UserAchievement.class));
    }

    @Test
    void existsByUsername_returnsRepositoryValue() {
        when(userRepo.existsByUsername("suji")).thenReturn(true);

        boolean exists = service.existsByUsername("suji");

        assertTrue(exists);
        verify(userRepo).existsByUsername("suji");
    }

    @Test
    void findByUsername_returnsRepositoryList() {
        List<User> users = List.of(user(1L, 0, 1), user(2L, 10, 1));
        when(userRepo.findByUsername("abc")).thenReturn(users);

        List<User> result = service.findByUsername("abc");

        assertSame(users, result);
        verify(userRepo).findByUsername("abc");
    }

    @Test
    void findById_whenFound_returnsUser() {
        User u = user(1L, 100, 1);
        when(userRepo.findById(1L)).thenReturn(Optional.of(u));

        User result = service.findById(1L);

        assertSame(u, result);
        verify(userRepo).findById(1L);
    }

    @Test
    void findById_whenMissing_returnsNull() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        User result = service.findById(99L);

        assertNull(result);
        verify(userRepo).findById(99L);
    }
}

