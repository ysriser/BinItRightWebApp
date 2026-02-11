package tech3.binitright.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class UserAchievementTest {
    @Test
    void settersAndGetters_shouldWork() {
        UserAchievement ua = new UserAchievement();

        User user = new User();
        Achievement achievement = new Achievement();

        ua.setUserAchievementId(1L);
        ua.setUser(user);
        ua.setAchievement(achievement);

        assertEquals(1L, ua.getUserAchievementId());
        assertSame(user, ua.getUser());
        assertSame(achievement, ua.getAchievement());
    }

    @Test
    void constructor_shouldSetFields() {
        User user = new User();
        Achievement achievement = new Achievement();

        UserAchievement ua = new UserAchievement(10L, user, achievement);

        assertEquals(10L, ua.getUserAchievementId());
        assertSame(user, ua.getUser());
        assertSame(achievement, ua.getAchievement());
    }
}
