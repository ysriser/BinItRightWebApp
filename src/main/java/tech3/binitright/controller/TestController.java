package tech3.binitright.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling home page requests.
 */
@Controller
public final class TestController { // Added 'final' here

    /**
     * Maps the root URL to the home view.
     * @return the name of the home template
     */
    @GetMapping("/")
    public String test() {
        return "home";
    }
}