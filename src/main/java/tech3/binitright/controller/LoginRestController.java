package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Admin;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class LoginRestController {

    @Autowired
    private AdminInterface adminService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<?> saveAdmin(@RequestBody Admin admin) {

        if (adminService.findAdminByUsername(admin.getUsername()).isEmpty()) {

            // HASH password
            admin.setPassword_hash(
                    passwordEncoder.encode(admin.getPassword_hash())
            );

            adminService.saveAdmin(admin);

            return ResponseEntity.ok(
                    Map.of("message", "Admin Account created successfully")
            );
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Username already exists"));
    }
}
