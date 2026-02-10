package tech3.binitright.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import tech3.binitright.response.RecycleHistoryResponse;
import tech3.binitright.service.RecycleHistoryService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RecycleHistoryControllerTest {

    private RecycleHistoryController controller;
    private RecycleHistoryService recycleHistoryService;

    @BeforeEach
    void setUp() {
        recycleHistoryService = Mockito.mock(RecycleHistoryService.class);
        controller = new RecycleHistoryController(recycleHistoryService);
    }

    @Test
    void getRecycleHistory_AuthenticatedUser_ReturnsHistory() {
        // Arrange
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("123");
        RecycleHistoryResponse record = new RecycleHistoryResponse(
                "Plastic",
                "plastic-icon.png",
                LocalDateTime.now(),
                5
        );

        List<RecycleHistoryResponse> mockHistory = List.of(record);
        when(recycleHistoryService.getRecycleHistory(123L)).thenReturn(mockHistory);

        // Act
        List<RecycleHistoryResponse> result = controller.getRecycleHistory(auth);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Plastic", result.get(0).getCategoryName());
    }

    @Test
    void getRecycleHistory_Unauthenticated_ThrowsException() {
        // Arrange
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            controller.getRecycleHistory(auth);
        });

        assertEquals("Unauthorized", exception.getMessage());
    }
}