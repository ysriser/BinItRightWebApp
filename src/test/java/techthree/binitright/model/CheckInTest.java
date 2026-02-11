package techthree.binitright.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class CheckInTest {
    @Test
    void settersAndGetters_shouldWork() {
        CheckIn checkIn = new CheckIn();

        User user = new User();
        DropOffLocation dropOff = new DropOffLocation();
        WasteCategories waste = new WasteCategories();

        LocalDateTime t = LocalDateTime.of(2026, 2, 11, 10, 0);

        checkIn.setCheckInId(1L);
        checkIn.setUser(user);
        checkIn.setDropOffLocation(dropOff);
        checkIn.setWasteCategories(waste);
        checkIn.setFileName("img1.jpg");
        checkIn.setCheckInTime(t);
        checkIn.setStatus(CheckIn.Status.PROCESSING);
        checkIn.setQuantity(2);
        checkIn.setRewardPoints(50);
        checkIn.setDuration(120L);

        assertEquals(1L, checkIn.getCheckInId());
        assertSame(user, checkIn.getUser());
        assertSame(dropOff, checkIn.getDropOffLocation());
        assertSame(waste, checkIn.getWasteCategories());
        assertEquals("img1.jpg", checkIn.getFileName());
        assertEquals(t, checkIn.getCheckInTime());
        assertEquals(CheckIn.Status.PROCESSING, checkIn.getStatus());
        assertEquals(2, checkIn.getQuantity());
        assertEquals(50, checkIn.getRewardPoints());
        assertEquals(120L, checkIn.getDuration());
    }
}
