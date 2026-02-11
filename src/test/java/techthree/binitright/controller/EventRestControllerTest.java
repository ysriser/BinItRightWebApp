package techthree.binitright.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import techthree.binitright.interfacemethods.EventInterface;
import techthree.binitright.model.Event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class EventRestControllerTest {

    @Test
    void getEventsReturnsAllEventsWhenNoFilterProvided() {
        // Arrange
        final EventRestController controller = new EventRestController();
        final EventInterface eventService = Mockito.mock(EventInterface.class);
        ReflectionTestUtils.setField(controller, "eventService", eventService);


        final Event event1 = new Event(
                1L,
                "Community Cleanup",
                "Cleaning the local park",
                "Green Park",
                "123456",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "cleanup.jpg",
                Event.Status.APPROVED
        );

        when(eventService.getAllEvents()).thenReturn(List.of(event1));

        // Act
        final ResponseEntity<List<Event>> response = controller.getEvents(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Community Cleanup", response.getBody().get(0).getTitle());
    }

    @Test
    void getEventsReturnsUpcomingEventsWhenFilterIsUpcoming() {

        final EventRestController controller = new EventRestController();
        final EventInterface eventService = Mockito.mock(EventInterface.class);
        ReflectionTestUtils.setField(controller, "eventService", eventService);

        final Event upcomingEvent = new Event(
                2L,
                "Recycling Talk",
                "Learn to sort waste",
                "Town Hall",
                "654321",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(1),
                "talk.png",
                Event.Status.APPROVED
        );

        when(eventService.findByEndTimeAfterOrderByStartTimeAsc()).thenReturn(List.of(upcomingEvent));

        // Act
        final ResponseEntity<List<Event>> response = controller.getEvents("upcoming");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Recycling Talk", response.getBody().get(0).getTitle());
        assertEquals(1, response.getBody().size());
    }
}