package tech3.binitright.interfacemethods;

import java.util.List;

import tech3.binitright.model.Event;

public interface EventInterface {

    public List<Event> findByEndTimeAfterOrderByStartTimeAsc();

    List<Event> getAllEvents();
}
