package tech3.binitright.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class LeaderboardDTOTest {
    @Test
    void constructor_setsAllFieldsCorrectly() {
        LeaderboardDTO dto = new LeaderboardDTO(
                1L,
                "sujitha",
                150L
        );

        assertEquals(1L, dto.getUserId());
        assertEquals("sujitha", dto.getUsername());
        assertEquals(150L, dto.getTotalQuantity());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        LeaderboardDTO dto = new LeaderboardDTO(
                1L,
                "user1",
                100L
        );

        dto.setUserId(2L);
        dto.setUsername("user2");
        dto.setTotalQuantity(250L);

        assertEquals(2L, dto.getUserId());
        assertEquals("user2", dto.getUsername());
        assertEquals(250L, dto.getTotalQuantity());
    }
}

