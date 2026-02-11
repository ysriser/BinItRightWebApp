package techthree.binitright.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class RewardRedemptionTest {
    @Test
    void settersAndGetters_shouldWork() {
        RewardRedemption rr = new RewardRedemption();

        User user = new User();
        Reward reward = new Reward();
        LocalDateTime t = LocalDateTime.of(2026, 2, 11, 16, 0);

        rr.setRedemptionId(1L);
        rr.setUser(user);
        rr.setReward(reward);
        rr.setRedeemedPoints(150);
        rr.setRedeemedAt(t);

        assertEquals(1L, rr.getRedemptionId());
        assertSame(user, rr.getUser());
        assertSame(reward, rr.getReward());
        assertEquals(150, rr.getRedeemedPoints());
        assertEquals(t, rr.getRedeemedAt());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        User user = new User();
        Reward reward = new Reward();
        LocalDateTime t = LocalDateTime.of(2026, 2, 10, 9, 30);

        RewardRedemption rr = new RewardRedemption(
                10L,
                user,
                reward,
                200,
                t
        );

        assertEquals(10L, rr.getRedemptionId());
        assertSame(user, rr.getUser());
        assertSame(reward, rr.getReward());
        assertEquals(200, rr.getRedeemedPoints());
        assertEquals(t, rr.getRedeemedAt());
    }

}
