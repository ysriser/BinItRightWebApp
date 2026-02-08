package tech3.binitright.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.interfacemethods.EventInterface;
import tech3.binitright.model.Event;
import tech3.binitright.service.EventImplementation;

@RestController
@RequestMapping("/api/events")
public final class EventRestController {

    @Autowired
    private EventInterface eventService;

    @Autowired
    public void setEventService(final EventImplementation eventImplementation) {
        this.eventService = eventImplementation;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getEvents(@RequestParam(required = false) final String filter) {
        if ("upcoming".equalsIgnoreCase(filter)) {
            return ResponseEntity.ok(eventService.findByEndTimeAfterOrderByStartTimeAsc());
        }
        return ResponseEntity.ok(eventService.getAllEvents());
    }
}