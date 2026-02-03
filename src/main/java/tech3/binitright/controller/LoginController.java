package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.service.CheckInImplementation;
import tech3.binitright.service.ForecastService;

/**
 * Controller for handling home page requests.
 */
@Controller
public class LoginController {

    @Autowired
    private CheckInInterface checkInService;

    @GetMapping({"/", "/login"})
    public String login(Authentication authentication) {
        // If the user is already logged in, send them straight to the dashboard
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/admin/dashboard";
        }
        return "login"; // Otherwise, show the login page
    }
}