package tech3.binitright.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import tech3.binitright.repository.AchievementRepository;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class AchievementSeederTest {

    private AchievementSeeder achievementSeeder;
    private AchievementRepository achievementRepository;

    @BeforeEach
    void setUp() {
        achievementSeeder = new AchievementSeeder();
        achievementRepository = Mockito.mock(AchievementRepository.class);
    }

    @Test
    void seedAchievements_WhenRepoIsEmpty_ShouldSaveAll() throws Exception {

        when(achievementRepository.count()).thenReturn(0L);
        CommandLineRunner runner = achievementSeeder.seedAchievements(achievementRepository);
        runner.run();
        verify(achievementRepository, times(1)).saveAll(anyList());
        assertTrue(achievementSeeder.isSeedingComplete(), "Seeding flag should be true after execution");
    }

    @Test
    void seedAchievements_WhenRepoHasData_ShouldSkipSaving() throws Exception {

        when(achievementRepository.count()).thenReturn(10L);
        CommandLineRunner runner = achievementSeeder.seedAchievements(achievementRepository);
        runner.run();
        verify(achievementRepository, never()).saveAll(anyList());
        assertTrue(achievementSeeder.isSeedingComplete(), "Seeding flag should be true even if skipped");
    }
}
