package tech3.binitright.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.dto.LeaderboardDTO;
import tech3.binitright.interfacemethods.CheckInInterface;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class LeaderboardControllerTest {

    private LeaderboardController controller;
    private CheckInInterface checkInService;

    @BeforeEach
    void setUp() {
        controller = new LeaderboardController();
        checkInService = Mockito.mock(CheckInInterface.class);
        ReflectionTestUtils.setField(controller, "checkInService", checkInService);
    }

    @Test
    void getMonthlyLeaderboard_ReturnsListFromService() {
        LeaderboardDTO rank1 = new LeaderboardDTO(10L, "RecyclePro", 150L);
        LeaderboardDTO rank2 = new LeaderboardDTO(25L, "GreenUser", 120L);

        List<LeaderboardDTO> mockData = List.of(rank1, rank2);

        when(checkInService.getMonthlyLeaderboard()).thenReturn(mockData);

        // Act
        ResponseEntity<List<LeaderboardDTO>> response = controller.getMonthlyLeaderboard();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());


        assertEquals("RecyclePro", response.getBody().get(0).getUsername());
        assertEquals(150L, response.getBody().get(0).getTotalQuantity());
        assertEquals(10L, response.getBody().get(0).getUserId());
    }
}