package techthree.binitright.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class AchievementTest {
    @Test
    void defaultConstructor_and_setters_shouldWork() {
        Achievement achievement = new Achievement();

        achievement.setAchievementId(1L);
        achievement.setName("Recycler Pro");
        achievement.setDescription("Recycle 100 items");
        achievement.setCriteria("100_ITEMS");
        achievement.setBadgeIconUrl("badge.png");

        List<UserAchievement> userAchievements = new ArrayList<>();
        achievement.setUserAchievement(userAchievements);

        assertEquals(1L, achievement.getAchievementId());
        assertEquals("Recycler Pro", achievement.getName());
        assertEquals("Recycle 100 items", achievement.getDescription());
        assertEquals("100_ITEMS", achievement.getCriteria());
        assertEquals("badge.png", achievement.getBadgeIconUrl());
        assertSame(userAchievements, achievement.getUserAchievement());
    }

    @Test
    void parameterizedConstructor_shouldSetFields() {
        Achievement achievement = new Achievement(
                "Eco Hero",
                "Save 10kg CO2",
                "10_CO2",
                "eco.png"
        );

        assertNull(achievement.getAchievementId()); // not set yet
        assertEquals("Eco Hero", achievement.getName());
        assertEquals("Save 10kg CO2", achievement.getDescription());
        assertEquals("10_CO2", achievement.getCriteria());
        assertEquals("eco.png", achievement.getBadgeIconUrl());
    }

}
