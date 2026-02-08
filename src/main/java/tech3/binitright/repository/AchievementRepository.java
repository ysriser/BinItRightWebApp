package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech3.binitright.model.Achievement;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}