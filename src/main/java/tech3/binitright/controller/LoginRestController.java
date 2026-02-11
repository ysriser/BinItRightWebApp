package tech3.binitright.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.model.Admin;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class LoginRestController {

    private final AdminInterface adminService;
    private final PasswordEncoder passwordEncoder;

    public LoginRestController(AdminInterface adminService,
                         PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> saveAdmin(@RequestBody Admin admin) {

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
