package tech3.binitright.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.model.Admin;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public final class LoginRestController {

    @Autowired
    private AdminInterface adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<?> saveAdmin(@RequestBody final Admin admin) {
        if (adminService.findAdminByUsername(admin.getUsername()).isEmpty()) {
            admin.setPasswordHash(passwordEncoder.encode(admin.getPasswordHash()));
            adminService.saveAdmin(admin);
            return ResponseEntity.ok(Map.of("message", "Admin Account created successfully"));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Username already exists"));
    }
}