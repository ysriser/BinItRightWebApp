package tech3.binitright.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class AchievementDTOTest {
    @Test
    void constructorAndGetters_shouldSetAndReturnValuesCorrectly() {

        Long id = 1L;
        String name = "Eco Warrior";
        String description = "Recycle 100 items";
        String criteria = "100 recyclables";
        String badgeIconUrl = "eco_badge.png";
        boolean isUnlocked = true;


        AchievementDTO dto = new AchievementDTO(
                id,
                name,
                description,
                criteria,
                badgeIconUrl,
                isUnlocked
        );


        assertEquals(id, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(description, dto.getDescription());
        assertEquals(criteria, dto.getCriteria());
        assertEquals(badgeIconUrl, dto.getBadgeIconUrl());
        assertTrue(dto.getIsUnlocked());
    }
}
