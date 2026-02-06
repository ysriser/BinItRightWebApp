package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech3.binitright.model.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}