package tech3.binitright.service;

import org.springframework.stereotype.Service;
import tech3.binitright.request.AchievementDTO;
import tech3.binitright.model.Achievement;
import tech3.binitright.model.UserAchievement;
import tech3.binitright.repository.AchievementRepository;
import tech3.binitright.repository.UserAchievementRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AchievementImplementation {

    private final AchievementRepository achievementRepo;
    private final UserAchievementRepository userAchievementRepo;

    public AchievementImplementation(AchievementRepository achievementRepo, UserAchievementRepository userAchievementRepo) {
        this.achievementRepo = achievementRepo;
        this.userAchievementRepo = userAchievementRepo;
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
}