package tech3.binitright.request;

import org.junit.jupiter.api.Test;
import tech3.binitright.model.CheckIn;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class CheckInDataReqTest {
    @Test
    void gettersAndSetters_shouldWorkCorrectly() {
        CheckInDataReq req = new CheckInDataReq();

        LocalDateTime now = LocalDateTime.now();

        req.setUserId(1L);
        req.setBinId("BIN-101");
        req.setWasteCategory("Plastic");
        req.setCheckInTime(now);
        req.setStatus(CheckIn.Status.APPROVED); // use your actual enum value
        req.setQuantity(5);
        req.setRewardPoints(50);
        req.setDuration(120L);
        req.setVideoKey("video123");

        assertEquals(1L, req.getUserId());
        assertEquals("BIN-101", req.getBinId());
        assertEquals("Plastic", req.getWasteCategory());
        assertEquals(now, req.getCheckInTime());
        assertEquals(CheckIn.Status.APPROVED, req.getStatus());
        assertEquals(5, req.getQuantity());
        assertEquals(50, req.getRewardPoints());
        assertEquals(120L, req.getDuration());
        assertEquals("video123", req.getVideoKey());
    }

    @Test
    void toString_shouldContainFieldValues() {
        CheckInDataReq req = new CheckInDataReq();
        req.setUserId(2L);
        req.setBinId("BIN-202");
        req.setWasteCategory("Glass");

        String result = req.toString();

        assertNotNull(result);
        assertTrue(result.contains("userId=2"));
        assertTrue(result.contains("BIN-202"));
        assertTrue(result.contains("Glass"));
    }
}
