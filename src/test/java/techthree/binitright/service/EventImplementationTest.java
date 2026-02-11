package techthree.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import techthree.binitright.model.Event;
import techthree.binitright.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventImplementationTest {
    @Mock
    private EventRepository eventRepository;

    private EventImplementation service;

    @BeforeEach
    void setUp() {
        service = new EventImplementation();
        ReflectionTestUtils.setField(service, "eventRepository", eventRepository);
    }

    @Test
    void findByEndTimeAfterOrderByStartTimeAsc_callsRepoWithNowAndReturnsList() {
        when(eventRepository.findByEndTimeAfterOrderByStartTimeAsc(any(LocalDateTime.class)))
                .thenReturn(List.of(new Event(), new Event()));

        List<Event> result = service.findByEndTimeAfterOrderByStartTimeAsc();

        assertEquals(2, result.size());

        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(eventRepository).findByEndTimeAfterOrderByStartTimeAsc(captor.capture());

        // sanity check: the "now" passed should be very close to current time
        LocalDateTime passed = captor.getValue();
        assertNotNull(passed);
        assertTrue(passed.isBefore(LocalDateTime.now().plusSeconds(2)));
        assertTrue(passed.isAfter(LocalDateTime.now().minusSeconds(2)));
    }

    @Test
    void getAllEvents_returnsFindAll() {
        when(eventRepository.findAll()).thenReturn(List.of(new Event()));

        List<Event> result = service.getAllEvents();

        assertEquals(1, result.size());
        verify(eventRepository).findAll();
    }
}

