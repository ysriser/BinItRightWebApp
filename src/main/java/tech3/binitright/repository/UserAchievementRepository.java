package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech3.binitright.model.UserAchievement;
import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUser_Id(Long userId);
}