package techthree.binitright.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShopItemDTOTest {


    @Test
    void allArgsConstructor_setsAllFieldsCorrectly() {
        ShopItemDTO dto = new ShopItemDTO(
                10L,
                "Cool Hat",
                150,
                true,
                false
        );

        assertEquals(10L, dto.getAccessoriesId());
        assertEquals("Cool Hat", dto.getName());
        assertEquals(150, dto.getRequiredPoints());
        assertTrue(dto.isOwned());
        assertFalse(dto.isEquipped());
    }

    @Test
    void booleanFlags_areHandledCorrectly() {
        ShopItemDTO ownedAndEquipped = new ShopItemDTO(
                1L,
                "Avatar",
                200,
                true,
                true
        );

        assertTrue(ownedAndEquipped.isOwned());
        assertTrue(ownedAndEquipped.isEquipped());
    }
}
