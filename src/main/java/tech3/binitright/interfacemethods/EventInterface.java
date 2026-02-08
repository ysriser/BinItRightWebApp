package tech3.binitright.interfacemethods;

import tech3.binitright.model.Event;

import java.util.List;

public interface EventInterface {

    public List<Event> findByEndTimeAfterOrderByStartTimeAsc();

    List<Event> getAllEvents();
}
