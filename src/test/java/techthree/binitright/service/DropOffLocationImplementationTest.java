package techthree.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import techthree.binitright.request.NearByBinDto;
import techthree.binitright.model.DropOffLocation;
import techthree.binitright.repository.DropOffLocationRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DropOffLocationImplementationTest {
    @Mock
    private DropOffLocationRepository repository;

    private DropOffLocationImplementation service;

    @BeforeEach
    void setUp() {
        service = new DropOffLocationImplementation();
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    // ---------- helpers ----------
    private DropOffLocation bin(
            String id,
            String name,
            String type,
            double lat,
            double lng
    ) {
        DropOffLocation b = new DropOffLocation();
        b.setId(id);
        b.setName(name);
        b.setAddress("Addr " + id);
        b.setDescription("Desc " + id);
        b.setPostalCode("000" + id);
        b.setBinType(type);
        // set your enum type accordingly; if Status is enum, use your correct value
        b.setStatus(DropOffLocation.Status.ACTIVE);
        b.setLatitude(BigDecimal.valueOf(lat));
        b.setLongitude(BigDecimal.valueOf(lng));
        return b;
    }

    @Test
    void getAllBins_returnsRepositoryFindAll() {
        when(repository.findAll()).thenReturn(List.of(new DropOffLocation(), new DropOffLocation()));

        List<DropOffLocation> result = service.getAllBins();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void getNearbyBins_filtersByRadius_sortsByDistance_limitsTo3() {
        // user near Singapore (example)
        double userLat = 1.3000;
        double userLng = 103.8000;

        // create 4 bins, 3 are close-ish, 1 is far
        DropOffLocation b1 = bin("1", "Bin1", "EWaste", 1.3001, 103.8001); // very near
        DropOffLocation b2 = bin("2", "Bin2", "BlueBin", 1.3010, 103.8010); // near
        DropOffLocation b3 = bin("3", "Bin3", "Lighting", 1.3020, 103.8020); // near
        DropOffLocation b4 = bin("4", "Bin4", "EWaste", 1.3500, 103.9000); // far

        when(repository.findAll()).thenReturn(List.of(b4, b3, b2, b1));

        // radius large enough for b1,b2,b3 but small enough to exclude b4
        double radiusMeters = 2000; // 2km

        List<NearByBinDto> res = service.getNearbyBins(userLat, userLng, radiusMeters);

        assertTrue(res.size() <= 3);
        assertEquals(3, res.size(), "Should return top 3 within radius");

        // must be sorted by nearest first
        assertTrue(res.get(0).getDistanceMeters() <= res.get(1).getDistanceMeters());
        assertTrue(res.get(1).getDistanceMeters() <= res.get(2).getDistanceMeters());

        // far bin excluded
        List<String> ids = res.stream().map(NearByBinDto::getId).toList();
        assertFalse(ids.contains("4"));

        verify(repository).findAll();
    }

    @Test
    void getNearbyBins_whenRadiusTooSmall_returnsEmpty() {
        double userLat = 1.3000;
        double userLng = 103.8000;

        DropOffLocation b1 = bin("1", "Bin1", "EWaste", 1.3001, 103.8001);

        when(repository.findAll()).thenReturn(List.of(b1));

        // too small radius (e.g., 1 meter)
        List<NearByBinDto> res = service.getNearbyBins(userLat, userLng, 1);

        assertTrue(res.isEmpty());
    }

    @Test
    void searchBins_typeAll_returnsAllSortedByDistance() {
        double userLat = 1.3000;
        double userLng = 103.8000;

        DropOffLocation near = bin("1", "Near", "EWaste", 1.3001, 103.8001);
        DropOffLocation far  = bin("2", "Far",  "BlueBin", 1.3200, 103.8200);

        when(repository.findAll()).thenReturn(List.of(far, near));

        List<NearByBinDto> res = service.searchBins(userLat, userLng, "All");

        assertEquals(2, res.size());
        assertEquals("1", res.get(0).getId(), "Nearest should be first");
        assertTrue(res.get(0).getDistanceMeters() <= res.get(1).getDistanceMeters());
    }

    @Test
    void searchBins_whenTypeFilterProvided_filtersByTypeOnly() {
        double userLat = 1.3000;
        double userLng = 103.8000;

        DropOffLocation e1 = bin("1", "E1", "EWaste", 1.3001, 103.8001);
        DropOffLocation b1 = bin("2", "B1", "BlueBin", 1.3002, 103.8002);
        DropOffLocation e2 = bin("3", "E2", "EWaste", 1.3300, 103.8300);

        when(repository.findAll()).thenReturn(List.of(e2, b1, e1));

        List<NearByBinDto> res = service.searchBins(userLat, userLng, "EWaste");

        assertEquals(2, res.size());
        assertTrue(res.stream().allMatch(dto -> dto.getBinType().equalsIgnoreCase("EWaste")));

        // sorted by distance
        assertTrue(res.get(0).getDistanceMeters() <= res.get(1).getDistanceMeters());
    }

    @Test
    void searchBins_whenTypeNullOrBlank_doesNotFilter() {
        double userLat = 1.3000;
        double userLng = 103.8000;

        DropOffLocation e1 = bin("1", "E1", "EWaste", 1.3001, 103.8001);
        DropOffLocation b1 = bin("2", "B1", "BlueBin", 1.3002, 103.8002);

        when(repository.findAll()).thenReturn(List.of(b1, e1));

        List<NearByBinDto> resNull = service.searchBins(userLat, userLng, null);
        List<NearByBinDto> resBlank = service.searchBins(userLat, userLng, "   ");

        assertEquals(2, resNull.size());
        assertEquals(2, resBlank.size());
    }
}
