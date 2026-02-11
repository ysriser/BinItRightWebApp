package techthree.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techthree.binitright.interfacemethods.EventInterface;
import techthree.binitright.model.Event;
import techthree.binitright.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventImplementation implements EventInterface {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> findByEndTimeAfterOrderByStartTimeAsc() {
        // This gets the current YYYY-MM-DD HH:MM:SS
        return eventRepository.findByEndTimeAfterOrderByStartTimeAsc(LocalDateTime.now());
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
