package tech3.binitright.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech3.binitright.interfacemethods.EventInterface;
import tech3.binitright.model.Event;
import tech3.binitright.repository.EventRepository;

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
