package tech3.binitright.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class AccessoriesTest {
    @Test
    void defaultConstructor_and_setters_shouldWork() {
        Accessories acc = new Accessories();

        acc.setAccessoriesId(1L);
        acc.setName("Hat");
        acc.setImageUrl("https://example.com/hat.png");
        acc.setRequiredPoints(120);

        List<UserAccessories> ua = new ArrayList<>();
        acc.setUserAccessories(ua);

        assertEquals(1L, acc.getAccessoriesId());
        assertEquals("Hat", acc.getName());
        assertEquals("https://example.com/hat.png", acc.getImageUrl());
        assertEquals(120, acc.getRequiredPoints());
        assertSame(ua, acc.getUserAccessories());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        List<UserAccessories> ua = new ArrayList<>();
        Accessories acc = new Accessories(
                99L,
                "Glasses",
                "https://example.com/glasses.png",
                300,
                ua
        );

        assertEquals(99L, acc.getAccessoriesId());
        assertEquals("Glasses", acc.getName());
        assertEquals("https://example.com/glasses.png", acc.getImageUrl());
        assertEquals(300, acc.getRequiredPoints());
        assertSame(ua, acc.getUserAccessories());
    }
}
