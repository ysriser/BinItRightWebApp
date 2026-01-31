package tech3.binitright.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for testing system connectivity.
 */
@RestController
public final class TestController { // Added 'final' here

    @GetMapping("/test")
    public String test() {
        return "Connected";
    }
}