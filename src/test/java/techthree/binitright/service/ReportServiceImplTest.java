package techthree.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import techthree.binitright.repository.WasteCheckinRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private WasteCheckinRepository checkinRepository;

    private ReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReportServiceImpl();

        try {
            var field = ReportServiceImpl.class.getDeclaredField("checkinRepository");
            field.setAccessible(true);
            field.set(service, checkinRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSustainabilityStats_whenRepoReturnsNumbers_returnsCorrectStats() {

        int month = 2, year = 2026;

        when(checkinRepository.calculateWeightByMonth(month, year))
                .thenReturn(100.0);

        when(checkinRepository.calculateCO2ByMonth(month, year))
                .thenReturn(25.0);

        when(checkinRepository.countParticipantsByMonth(month, year))
                .thenReturn(5L);


        Map<String, Object> stats = service.getSustainabilityStats(month, year);

        assertEquals(100.0, stats.get("totalWaste"));
        assertEquals(25.0, stats.get("co2Saved"));
        assertEquals(5L, stats.get("activeParticipants"));
    }

    @Test
    void getSustainabilityStats_whenRepoReturnsNull_defaultsToZero() {

        int month = 1, year = 2025;

        when(checkinRepository.calculateWeightByMonth(month, year))
                .thenReturn(null);

        when(checkinRepository.calculateCO2ByMonth(month, year))
                .thenReturn(null);

        when(checkinRepository.countParticipantsByMonth(month, year))
                .thenReturn(null);

        when(checkinRepository.getWeightDistributionByMonth(month, year))
                .thenReturn(List.of());

        Map<String, Object> stats = service.getSustainabilityStats(month, year);

        assertEquals(0.0, stats.get("totalWaste"));
        assertEquals(0.0, stats.get("co2Saved"));
        assertEquals(0L, stats.get("activeParticipants"));
        assertEquals("N/A", stats.get("mostRecycled"));
        assertEquals(0L, stats.get("mostRecycledPercent"));
    }

    @Test
    void getSustainabilityStats_whenRepoReturnsInteger_castsProperly() {

        int month = 12, year = 2024;

        when(checkinRepository.calculateWeightByMonth(month, year))
                .thenReturn(50);  // Integer

        when(checkinRepository.calculateCO2ByMonth(month, year))
                .thenReturn(10L); // Long

        when(checkinRepository.countParticipantsByMonth(month, year))
                .thenReturn(2L);

        Map<String, Object> stats = service.getSustainabilityStats(month, year);

        assertEquals(50.0, stats.get("totalWaste"));
        assertEquals(10.0, stats.get("co2Saved"));
        assertEquals(2L, stats.get("activeParticipants"));
    }
}
