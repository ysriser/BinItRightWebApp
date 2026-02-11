package techthree.binitright.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling home page requests.
 */
@Controller
public class LoginController {

    @GetMapping({"/", "/login"})
    public String login(Authentication authentication) {
        // If the user is already logged in, send them straight to the dashboard
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/admin/dashboard";
        }
        return "login"; // Otherwise, show the login page
    }
}