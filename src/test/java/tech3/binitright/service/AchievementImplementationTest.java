package tech3.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tech3.binitright.model.Achievement;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAchievement;
import tech3.binitright.repository.AchievementRepository;
import tech3.binitright.repository.UserAchievementRepository;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.request.AchievementDTO;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AchievementImplementationTest {
    @Mock AchievementRepository achievementRepo;
    @Mock UserAchievementRepository userAchievementRepo;
    @Mock UserRepository userRepo;

    @InjectMocks AchievementImplementation service;

    private Achievement achievement(long id) {
        Achievement a = new Achievement();
        a.setAchievementId(id);
        a.setName("A" + id);
        return a;
    }

    private User user(long id, Integer points, Integer rank) {
        User u = new User();
        u.setId(id);
        u.setPointBalance(points);
        u.setCurrentRank(rank);
        return u;
    }

    @Test
    void checkProfileAchievements_points5000_unlocks5() {
        User u = user(10L, 5000, 1);

        when(userAchievementRepo.findByUser_Id(10L)).thenReturn(Collections.emptyList());
        when(userRepo.findById(10L)).thenReturn(Optional.of(u));
        when(achievementRepo.findById(5L)).thenReturn(Optional.of(achievement(5L)));

        service.checkProfileAchievements(u);

        ArgumentCaptor<UserAchievement> cap = ArgumentCaptor.forClass(UserAchievement.class);
        verify(userAchievementRepo).save(cap.capture());
        assertEquals(5L, cap.getValue().getAchievement().getAchievementId());
    }

    @Test
    void checkProfileAchievements_rank2_unlocks6() {
        User u = user(10L, 100, 2);

        when(userAchievementRepo.findByUser_Id(10L)).thenReturn(Collections.emptyList());
        when(userRepo.findById(10L)).thenReturn(Optional.of(u));
        when(achievementRepo.findById(6L)).thenReturn(Optional.of(achievement(6L)));

        service.checkProfileAchievements(u);

        ArgumentCaptor<UserAchievement> cap = ArgumentCaptor.forClass(UserAchievement.class);
        verify(userAchievementRepo).save(cap.capture());
        assertEquals(6L, cap.getValue().getAchievement().getAchievementId());
    }

    @Test
    void checkProfileAchievements_pointsAndRank_unlocksBoth() {
        User u = user(10L, 6000, 3);

        // unlockAchievement calls findByUser_Id before each unlock
        when(userAchievementRepo.findByUser_Id(10L)).thenReturn(Collections.emptyList());
        when(userRepo.findById(10L)).thenReturn(Optional.of(u));
        when(achievementRepo.findById(5L)).thenReturn(Optional.of(achievement(5L)));
        when(achievementRepo.findById(6L)).thenReturn(Optional.of(achievement(6L)));

        service.checkProfileAchievements(u);

        ArgumentCaptor<UserAchievement> cap = ArgumentCaptor.forClass(UserAchievement.class);
        verify(userAchievementRepo, times(2)).save(cap.capture());

        var ids = cap.getAllValues().stream()
                .map(x -> x.getAchievement().getAchievementId())
                .toList();

        assertTrue(ids.contains(5L));
        assertTrue(ids.contains(6L));
    }

    @Test
    void checkProfileAchievements_pointsNull_doesNotUnlockPointsAchievement() {
        User u = user(10L, null, 1);

        service.checkProfileAchievements(u);

        verify(userAchievementRepo, never()).save(any());
        verifyNoInteractions(userRepo);
        verifyNoInteractions(achievementRepo);
    }
}


