package tech3.binitright.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class EventTest {
    @Test
    void settersAndGetters_shouldWork() {
        Event event = new Event();

        LocalDateTime start = LocalDateTime.of(2026, 2, 11, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 11, 12, 0);

        event.setEventId(1L);
        event.setTitle("Recycling Drive");
        event.setDescription("Bring your recyclables");
        event.setLocationName("NUS-ISS");
        event.setPostalCode("119077");
        event.setStartTime(start);
        event.setEndTime(end);
        event.setImageUrl("event.png");
        event.setStatus(Event.Status.APPROVED);

        assertEquals(1L, event.getEventId());
        assertEquals("Recycling Drive", event.getTitle());
        assertEquals("Bring your recyclables", event.getDescription());
        assertEquals("NUS-ISS", event.getLocationName());
        assertEquals("119077", event.getPostalCode());
        assertEquals(start, event.getStartTime());
        assertEquals(end, event.getEndTime());
        assertEquals("event.png", event.getImageUrl());
        assertEquals(Event.Status.APPROVED, event.getStatus());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 1, 11, 0);

        Event event = new Event(
                10L,
                "Cleanup Event",
                "Community cleanup",
                "West Coast Park",
                "127708",
                start,
                end,
                "cleanup.jpg",
                Event.Status.PROCESSING
        );

        assertEquals(10L, event.getEventId());
        assertEquals("Cleanup Event", event.getTitle());
        assertEquals("Community cleanup", event.getDescription());
        assertEquals("West Coast Park", event.getLocationName());
        assertEquals("127708", event.getPostalCode());
        assertEquals(start, event.getStartTime());
        assertEquals(end, event.getEndTime());
        assertEquals("cleanup.jpg", event.getImageUrl());
        assertEquals(Event.Status.PROCESSING, event.getStatus());
    }

    @Test
    void setLocationName_shouldUpdateLocationName() {
        Event event = new Event();
        event.setLocationName("Location A");
        event.setLocationName("Location B");

        assertEquals("Location B", event.getLocationName());
    }
}
