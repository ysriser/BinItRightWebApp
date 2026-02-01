package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Admin;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class LoginRestController {

    @Autowired
    private UserInterface uservice;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<?> saveCustomer(@RequestBody Admin admin) {
        if(uservice.findAdminByUsername(admin.getUsername()).isEmpty()){
            // HASH THE PASSWORD BEFORE SAVING
            String encodedPassword = passwordEncoder.encode(admin.getPassword_hash());
            admin.setPassword_hash(encodedPassword);

            uservice.saveAdmin(admin);
            return new ResponseEntity<>(Map.of("message","Admin Account created successfully"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("error", "Username already exists"), HttpStatus.CONFLICT);
    }


}
