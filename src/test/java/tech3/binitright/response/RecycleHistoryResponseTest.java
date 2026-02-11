package tech3.binitright.response;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class RecycleHistoryResponseTest {
    @Test
    void constructor_shouldSetFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        RecycleHistoryResponse response =
                new RecycleHistoryResponse(
                        "Plastic",
                        "plastic_icon.png",
                        now,
                        5
                );

        assertEquals("Plastic", response.getCategoryName());
        assertEquals("plastic_icon.png", response.getCategoryIcon());
        assertEquals(now, response.getDate());
        assertEquals(5, response.getQuantity());
    }
}
