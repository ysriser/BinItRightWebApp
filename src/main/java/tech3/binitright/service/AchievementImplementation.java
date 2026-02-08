package tech3.binitright.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tech3.binitright.model.Achievement;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAchievement;
import tech3.binitright.repository.AchievementRepository;
import tech3.binitright.repository.UserAchievementRepository;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.request.AchievementDTO;

@Service
public final class AchievementImplementation {

    private final AchievementRepository achievementRepo;
    private final UserAchievementRepository userAchievementRepo;
    private final UserRepository userRepo;

    public AchievementImplementation(final AchievementRepository achievementRepo,
    		final UserAchievementRepository userAchievementRepo, final UserRepository userRepo) {
        this.achievementRepo = achievementRepo;
        this.userAchievementRepo = userAchievementRepo;
        this.userRepo = userRepo;
    }

    public List<AchievementDTO> getAchievementsForUser(final Long userId) {

        final List<Achievement> allAchievements = achievementRepo.findAll();

        final List<UserAchievement> userUnlocked = userAchievementRepo.findByUserUId(userId);
        final Set<Long> unlockedIds = userUnlocked.stream()
                .map(ua -> ua.getAchievement().getAchievementId())
                .collect(Collectors.toSet());

        return allAchievements.stream().map(ach -> new AchievementDTO(
                ach.getAchievementId(),
                ach.getName(),
                ach.getDescription(),
                ach.getCriteria(),
                ach.getBadgeIconUrl(),
                unlockedIds.contains(ach.getAchievementId())
        )).collect(Collectors.toList());
    }

    public int getTotalAchievements(final Long userId) {
        return userAchievementRepo.countByUserId(userId);
    }

    public void unlockAchievement(final Long userId, final Long achievementId) {
        final boolean alreadyUnlocked = userAchievementRepo.findByUserUId(userId).stream()
                .anyMatch(ua -> ua.getAchievement().getAchievementId().equals(achievementId));

        if (alreadyUnlocked) {
            return;
        }

        final User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final Achievement achievement = achievementRepo.findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));

        final UserAchievement newUnlock = new UserAchievement();
        newUnlock.setUser(user);
        newUnlock.setAchievement(achievement);

        userAchievementRepo.save(newUnlock);
    }

    public void checkProfileAchievements(final User user) {
        try {
            if (user.getPointBalance() != null && user.getPointBalance() >= 5000) {
                unlockAchievement(user.getId(), 5L);
            }

            if (user.getCurrentRank() >= 2) {
                unlockAchievement(user.getId(), 6L);
            }
        } catch (final Exception e) {
            System.err.println("Error checking profile achievements: " + e.getMessage());
        }
    }
}