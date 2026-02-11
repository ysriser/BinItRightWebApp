package tech3.binitright.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DropOffLocationTest {
    @Test
    void defaultConstructor_shouldInitializeCheckInList() {
        DropOffLocation loc = new DropOffLocation();
        assertNotNull(loc.getCheckIn());
        // in your entity it's initialized: new ArrayList<>()
        assertTrue(loc.getCheckIn().isEmpty());
    }

    @Test
    void settersAndGetters_shouldWork() {
        DropOffLocation loc = new DropOffLocation();

        List<CheckIn> checkIns = new ArrayList<>();

        loc.setId("DO-001");
        loc.setName("NUS Recycling Point");
        loc.setAddress("21 Lower Kent Ridge Rd");
        loc.setPostalCode("119077");
        loc.setDescription("Near the main entrance");
        loc.setBinType("Mixed Recycling");
        loc.setLatitude(new BigDecimal("1.2967"));
        loc.setLongitude(new BigDecimal("103.7764"));
        loc.setStatus(DropOffLocation.Status.ACTIVE);
        loc.setCheckIn(checkIns);

        assertEquals("DO-001", loc.getId());
        assertEquals("NUS Recycling Point", loc.getName());
        assertEquals("21 Lower Kent Ridge Rd", loc.getAddress());
        assertEquals("119077", loc.getPostalCode());
        assertEquals("Near the main entrance", loc.getDescription());
        assertEquals("Mixed Recycling", loc.getBinType());
        assertEquals(new BigDecimal("1.2967"), loc.getLatitude());
        assertEquals(new BigDecimal("103.7764"), loc.getLongitude());
        assertEquals(DropOffLocation.Status.ACTIVE, loc.getStatus());
        assertSame(checkIns, loc.getCheckIn());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        List<CheckIn> checkIns = new ArrayList<>();
        DropOffLocation loc = new DropOffLocation(
                "DO-999",
                "Test Location",
                "Test Address",
                "000000",
                "Test Desc",
                "E-waste",
                new BigDecimal("1.0000"),
                new BigDecimal("103.0000"),
                DropOffLocation.Status.OPEN,
                checkIns,
                "ignoredParam"
        );

        assertEquals("DO-999", loc.getId());
        assertEquals("Test Location", loc.getName());
        assertEquals("Test Address", loc.getAddress());
        assertEquals("000000", loc.getPostalCode());
        assertEquals("Test Desc", loc.getDescription());
        assertEquals("E-waste", loc.getBinType());
        assertEquals(new BigDecimal("1.0000"), loc.getLatitude());
        assertEquals(new BigDecimal("103.0000"), loc.getLongitude());
        assertEquals(DropOffLocation.Status.OPEN, loc.getStatus());
        assertSame(checkIns, loc.getCheckIn());
    }
}

