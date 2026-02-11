package techthree.binitright.service;

import org.springframework.stereotype.Service;
import techthree.binitright.request.AchievementDTO;
import techthree.binitright.model.Achievement;
import techthree.binitright.model.User;
import techthree.binitright.model.UserAchievement;
import techthree.binitright.repository.AchievementRepository;
import techthree.binitright.repository.UserAchievementRepository;
import techthree.binitright.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AchievementImplementation {

    Logger logger = Logger.getLogger(getClass().getName());

    private final AchievementRepository achievementRepo;
    private final UserAchievementRepository userAchievementRepo;
    private final UserRepository userRepo;

    public AchievementImplementation(AchievementRepository achievementRepo, UserAchievementRepository userAchievementRepo, UserRepository userRepo) {
        this.achievementRepo = achievementRepo;
        this.userAchievementRepo = userAchievementRepo;
        this.userRepo = userRepo;
    }

    public List<AchievementDTO> getAchievementsForUser(Long userId) {

        List<Achievement> allAchievements = achievementRepo.findAll();

        List<UserAchievement> userUnlocked = userAchievementRepo.findByUser_Id(userId);
        Set<Long> unlockedIds = userUnlocked.stream()
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

    public int getTotalAchievements(Long userId) {
        return userAchievementRepo.countByUserId(userId);
    }

    public void unlockAchievement(Long userId, Long achievementId) {
        boolean alreadyUnlocked = userAchievementRepo.findByUser_Id(userId).stream()
                .anyMatch(ua -> ua.getAchievement().getAchievementId().equals(achievementId));

        if (alreadyUnlocked) {
            return;
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Achievement achievement = achievementRepo.findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));

        UserAchievement newUnlock = new UserAchievement();
        newUnlock.setUser(user);
        newUnlock.setAchievement(achievement);

        userAchievementRepo.save(newUnlock);
    }

    public void checkProfileAchievements(User user) {
        try {
            if (user.getPointBalance() != null && user.getPointBalance() >= 5000) {
                unlockAchievement(user.getId(), 5L);
            }

            if (user.getCurrentRank() >= 2) {
                unlockAchievement(user.getId(), 6L);
            }
        } catch (Exception e) {
            logger.info("Error checking profile achievements: " + e.getMessage());
        }
    }
}