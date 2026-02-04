package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.EventInterface;
import tech3.binitright.model.Event;
import tech3.binitright.service.EventImplementation;

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