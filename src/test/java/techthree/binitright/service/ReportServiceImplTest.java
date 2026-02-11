package techthree.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import techthree.binitright.repository.WasteCheckinRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {
    @Mock
    private WasteCheckinRepository checkinRepository;

    private ReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReportServiceImpl();

        // since your service uses @Autowired field injection, we set it manually
        // (no Spring context needed)
        try {
            var f = ReportServiceImpl.class.getDeclaredField("checkinRepository");
            f.setAccessible(true);
            f.set(service, checkinRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSustainabilityStats_whenRepoReturnsNumbers_returnsStats() {
        int month = 2, year = 2026;

        when(checkinRepository.calculateWeightByMonth(month, year)).thenReturn(12.5);
        when(checkinRepository.calculateCO2ByMonth(month, year)).thenReturn(3.2);
        when(checkinRepository.countParticipantsByMonth(month, year)).thenReturn(7L);

        Map<String, Object> stats = service.getSustainabilityStats(month, year);

        assertEquals(12.5, (Double) stats.get("totalWaste"));
        assertEquals(7L, stats.get("activeParticipants"));
        assertEquals(3.2, (Double) stats.get("co2Saved"));

        assertEquals("N/A", stats.get("mostRecycled"));
        assertEquals(0, stats.get("mostRecycledPercent"));
        assertEquals(78, stats.get("recyclingRate"));

        verify(checkinRepository).calculateWeightByMonth(month, year);
        verify(checkinRepository).calculateCO2ByMonth(month, year);
        verify(checkinRepository).countParticipantsByMonth(month, year);
    }

    @Test
    void getSustainabilityStats_whenRepoReturnsNullOrNonNumber_defaultsToZero() {
        int month = 1, year = 2025;

        when(checkinRepository.calculateWeightByMonth(month, year)).thenReturn(null);
        when(checkinRepository.calculateCO2ByMonth(month, year)).thenReturn("not-a-number");
        when(checkinRepository.countParticipantsByMonth(month, year)).thenReturn(null);

        Map<String, Object> stats = service.getSustainabilityStats(month, year);

        assertEquals(0.0, (Double) stats.get("totalWaste"));
        assertEquals(0.0, (Double) stats.get("co2Saved"));
        assertEquals(0L, stats.get("activeParticipants"));
    }

    @Test
    void getSustainabilityStats_whenRepoReturnsInteger_castsToDouble() {
        int month = 12, year = 2024;

        when(checkinRepository.calculateWeightByMonth(month, year)).thenReturn(20); // Integer
        when(checkinRepository.calculateCO2ByMonth(month, year)).thenReturn(5L);    // Long
        when(checkinRepository.countParticipantsByMonth(month, year)).thenReturn(2L);

        Map<String, Object> stats = service.getSustainabilityStats(month, year);

        assertEquals(20.0, (Double) stats.get("totalWaste"));
        assertEquals(5.0, (Double) stats.get("co2Saved"));
        assertEquals(2L, stats.get("activeParticipants"));
    }
}

