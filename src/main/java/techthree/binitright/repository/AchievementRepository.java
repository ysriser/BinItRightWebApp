package techthree.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techthree.binitright.model.Achievement;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}