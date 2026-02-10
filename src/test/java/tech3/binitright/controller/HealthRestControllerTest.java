package tech3.binitright.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.config.AchievementSeeder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthRestControllerTest {

    @Test
    void returnsReadyWhenSeedingComplete() {
        final HealthRestController controller = new HealthRestController();
        final AchievementSeeder seeder = new AchievementSeeder() {
            @Override
            public boolean isSeedingComplete() {
                return true;
            }
        };
        ReflectionTestUtils.setField(controller, "achievementSeeder", seeder);

        final ResponseEntity<String> response = controller.checkReadiness();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("READY", response.getBody());
    }

    @Test
    void returnsServiceUnavailableWhenSeedingInProgress() {
        final HealthRestController controller = new HealthRestController();
        final AchievementSeeder seeder = new AchievementSeeder() {
            @Override
            public boolean isSeedingComplete() {
                return false;
            }
        };
        ReflectionTestUtils.setField(controller, "achievementSeeder", seeder);

        final ResponseEntity<String> response = controller.checkReadiness();

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("SEEDING_IN_PROGRESS", response.getBody());
    }
}
