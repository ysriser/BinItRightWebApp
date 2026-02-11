package techthree.binitright.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import techthree.binitright.dto.EmissionCalculator;
import techthree.binitright.model.CheckIn;
import techthree.binitright.model.WasteCategories;
import techthree.binitright.repository.CheckInRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class EmissionServiceTest {
    @Mock
    private CheckInRepository checkInRepository;

    private EmissionService service;

    @BeforeEach
    void setUp() {
        service = new EmissionService(checkInRepository);
    }

    private WasteCategories wc(BigDecimal avgWeight, BigDecimal emissionFactor) {
        WasteCategories w = new WasteCategories();
        w.setAvgWeight(avgWeight);
        w.setEmissionFactor(emissionFactor);
        return w;
    }

    private CheckIn checkIn(int qty, WasteCategories wc) {
        CheckIn c = new CheckIn();
        c.setQuantity(qty);
        c.setWasteCategories(wc);
        return c;
    }

    @Test
    void getUserTotalCo2Saved_sumsCalculatedCo2_forApprovedCheckIns() {
        Long userId = 10L;

        WasteCategories wc1 = wc(new BigDecimal("0.50"), new BigDecimal("1.20"));
        WasteCategories wc2 = wc(new BigDecimal("0.30"), new BigDecimal("0.80"));

        CheckIn ci1 = checkIn(2, wc1);
        CheckIn ci2 = checkIn(5, wc2);

        when(checkInRepository.findApprovedByUserIdWithCategory(userId))
                .thenReturn(List.of(ci1, ci2));

        BigDecimal result = service.getUserTotalCo2Saved(userId);

        // expected = sum of the same calculator your service uses
        BigDecimal expected =
                EmissionCalculator.co2SavedKg(2, wc1.getAvgWeight(), wc1.getEmissionFactor())
                        .add(EmissionCalculator.co2SavedKg(5, wc2.getAvgWeight(), wc2.getEmissionFactor()));

        assertEquals(0, expected.compareTo(result));
        verify(checkInRepository).findApprovedByUserIdWithCategory(userId);
    }

    @Test
    void getUserTotalCo2Saved_skipsNullWasteCategory() {
        Long userId = 10L;

        WasteCategories wc1 = wc(new BigDecimal("0.50"), new BigDecimal("1.20"));

        CheckIn ci1 = checkIn(2, wc1);
        CheckIn ciNull = checkIn(10, null); // should be skipped

        when(checkInRepository.findApprovedByUserIdWithCategory(userId))
                .thenReturn(List.of(ci1, ciNull));

        BigDecimal result = service.getUserTotalCo2Saved(userId);

        BigDecimal expected = EmissionCalculator.co2SavedKg(
                2, wc1.getAvgWeight(), wc1.getEmissionFactor()
        );

        assertEquals(0, expected.compareTo(result));
    }

    @Test
    void getUserTotalCo2Saved_whenNoApproved_returnsZero() {
        Long userId = 10L;

        when(checkInRepository.findApprovedByUserIdWithCategory(userId))
                .thenReturn(List.of());

        BigDecimal result = service.getUserTotalCo2Saved(userId);

        assertEquals(0, BigDecimal.ZERO.compareTo(result));
    }

    @Test
    void getPendingApprovals_returnsRepoCount() {
        Long userId = 10L;

        when(checkInRepository.countPendingByUserId(userId)).thenReturn(7L);

        Long result = service.getPendingApprovals(userId);

        assertEquals(7L, result);
        verify(checkInRepository).countPendingByUserId(userId);
    }
}

