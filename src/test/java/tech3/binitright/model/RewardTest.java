package tech3.binitright.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class RewardTest {
    @Test
    void settersAndGetters_shouldWork() {
        Reward reward = new Reward();

        List<RewardRedemption> redemptions = new ArrayList<>();

        reward.setRewardId(1L);
        reward.setName("Reusable Bottle");
        reward.setDescription("Stainless steel bottle");
        reward.setPointsRequired(200);
        reward.setStock(10);
        reward.setStatus(Reward.RewardStatus.AVAILABLE);
        reward.setRewardRedemption(redemptions);

        assertEquals(1L, reward.getRewardId());
        assertEquals("Reusable Bottle", reward.getName());
        assertEquals("Stainless steel bottle", reward.getDescription());
        assertEquals(200, reward.getPointsRequired());
        assertEquals(10, reward.getStock());
        assertEquals(Reward.RewardStatus.AVAILABLE, reward.getStatus());
        assertSame(redemptions, reward.getRewardRedemption());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        List<RewardRedemption> redemptions = new ArrayList<>();

        Reward reward = new Reward(
                5L,
                "Eco Bag",
                "Canvas tote bag",
                120,
                25,
                Reward.RewardStatus.UNAVAILABLE,
                redemptions
        );

        assertEquals(5L, reward.getRewardId());
        assertEquals("Eco Bag", reward.getName());
        assertEquals("Canvas tote bag", reward.getDescription());
        assertEquals(120, reward.getPointsRequired());
        assertEquals(25, reward.getStock());
        assertEquals(Reward.RewardStatus.UNAVAILABLE, reward.getStatus());
        assertSame(redemptions, reward.getRewardRedemption());
    }

}
