package techthree.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techthree.binitright.interfacemethods.EventInterface;
import techthree.binitright.model.Event;
import techthree.binitright.service.EventImplementation;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    @Autowired
    private EventInterface eventService;

    @Autowired
    public void setEventService(EventImplementation eventImplementation) {
        this.eventService = eventImplementation;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getEvents(@RequestParam(required = false) String filter) {
        if ("upcoming".equalsIgnoreCase(filter)) {
            return ResponseEntity.ok(eventService.findByEndTimeAfterOrderByStartTimeAsc());
        }
        return ResponseEntity.ok(eventService.getAllEvents()); // For admin view
    }
}