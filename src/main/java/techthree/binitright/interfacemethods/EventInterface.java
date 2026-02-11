package techthree.binitright.interfacemethods;

import techthree.binitright.model.Event;

import java.util.List;

public interface EventInterface {

    public List<Event> findByEndTimeAfterOrderByStartTimeAsc();

    List<Event> getAllEvents();
}
