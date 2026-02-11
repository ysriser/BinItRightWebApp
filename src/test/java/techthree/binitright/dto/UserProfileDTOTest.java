package techthree.binitright.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserProfileDTOTest {
    @Test
    void allArgsConstructor_setsAllFieldsCorrectly() {
        UserProfileDTO dto = new UserProfileDTO(
                "User1",
                120,
                "CoolAvatar",
                15,
                "Great progress!",
                4,
                3.5f
        );

        assertEquals("User1", dto.getName());
        assertEquals(120, dto.getPointBalance());
        assertEquals("CoolAvatar", dto.getEquippedAvatarName());
        assertEquals(15, dto.getTotalRecycled());
        assertEquals("Great progress!", dto.getAiSummary());
        assertEquals(4, dto.getTotalAchievements());
        assertEquals(3.5f, dto.getCarbonEmissionSaved(), 0.0001f);
    }

    @Test
    void setters_updateFieldsCorrectly_exceptCarbonEmission() {
        UserProfileDTO dto = new UserProfileDTO();

        dto.setName("User");
        dto.setPointBalance(50);
        dto.setEquippedAvatarName("Avatar1");
        dto.setTotalRecycled(8);
        dto.setAiSummary("Nice work");
        dto.setTotalAchievements(2);

        assertEquals("User", dto.getName());
        assertEquals(50, dto.getPointBalance());
        assertEquals("Avatar1", dto.getEquippedAvatarName());
        assertEquals(8, dto.getTotalRecycled());
        assertEquals("Nice work", dto.getAiSummary());
        assertEquals(2, dto.getTotalAchievements());
    }
}
