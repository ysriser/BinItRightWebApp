package tech3.binitright.request;

import org.junit.jupiter.api.Test;
import tech3.binitright.model.DropOffLocation;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class NearByBinDtoTest {
    @Test
    void defaultConstructor_andSetters_shouldWork() {
        NearByBinDto dto = new NearByBinDto();

        dto.setId("BIN-1");
        dto.setName("Recycling Bin");
        dto.setAddress("123 Street");
        dto.setDescription("Plastic bin");
        dto.setPostalCode("123456");
        dto.setBinType("PLASTIC");
        dto.setLatitude(1.234);
        dto.setLongitude(103.123);
        dto.setDistanceMeters(250.0);
        dto.setStatus(DropOffLocation.Status.ACTIVE);

        assertEquals("BIN-1", dto.getId());
        assertEquals("Recycling Bin", dto.getName());
        assertEquals("123 Street", dto.getAddress());
        assertEquals("Plastic bin", dto.getDescription());
        assertEquals("123456", dto.getPostalCode());
        assertEquals("PLASTIC", dto.getBinType());
        assertEquals(1.234, dto.getLatitude());
        assertEquals(103.123, dto.getLongitude());
        assertEquals(250.0, dto.getDistanceMeters());
        assertEquals(DropOffLocation.Status.ACTIVE, dto.getStatus());
    }

    @Test
    void parameterizedConstructor_shouldConvertBigDecimalToDouble() {
        BigDecimal lat = new BigDecimal("1.3521");
        BigDecimal lon = new BigDecimal("103.8198");

        NearByBinDto dto = new NearByBinDto(
                "BIN-2",
                "Glass Bin",
                "456 Avenue",
                "Glass only",
                "654321",
                "GLASS",
                DropOffLocation.Status.ACTIVE,
                lat,
                lon,
                500.5
        );

        assertEquals("BIN-2", dto.getId());
        assertEquals(1.3521, dto.getLatitude());
        assertEquals(103.8198, dto.getLongitude());
        assertEquals(500.5, dto.getDistanceMeters());
    }
}
