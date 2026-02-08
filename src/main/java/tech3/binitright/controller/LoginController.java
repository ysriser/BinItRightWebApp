package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech3.binitright.interfacemethods.CheckInInterface;

/**
 * Controller for handling home page requests.
 */
@Controller
public final class LoginController { // 修复：声明类为 final

    @Autowired
    private CheckInInterface checkInService;

    @GetMapping({"/", "/login"})
    public String login(final Authentication authentication) { // 修复：添加 final 到参数
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/admin/dashboard";
        }
        return "login";
    }
}