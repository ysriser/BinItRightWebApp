package tech3.binitright.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;
import tech3.binitright.request.LoginRequest;
import tech3.binitright.request.RegisterRequest;
import tech3.binitright.response.LoginResponse;
import tech3.binitright.response.RegisterResponse;
import tech3.binitright.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserLoginController {
    @Autowired
    private UserInterface userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody final LoginRequest request) {

        // ✅ 1. Validate request body
        if (request.getUsername() == null || request.getPassword() == null) {
            return new LoginResponse(false, "Missing credentials", null);
        }

        // ✅ 2. Find APP USER (appUusers table)
        final List<User> users = userService.findByUsername(request.getUsername());

        if (users.isEmpty()) {
            return new LoginResponse(false, "Invalid username or password", null);
        }

        final User user = users.get(0);

        // ✅ 3. Match BCrypt password against passwordUhash
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordUhash()
        )) {
            return new LoginResponse(false, "Invalid username or password", null);
        }

        // ✅ 4. Generate JWT token
        final String token = jwtUtil.generateToken(user);
        System.out.println("User: " +  user.getId());

        // ✅ 5. Return success
        return new LoginResponse(true, "Login success", token);
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody final RegisterRequest req) {

        if (userService.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse(false, "Username already exists"));
        }

        final User user = new User();
        user.setUsername(req.getUsername());
        user.setPasswordUhash(passwordEncoder.encode(req.getPassword()));
        user.setRole("USER");
        userService.saveUser(user);

        return ResponseEntity.ok(new RegisterResponse(true, "Account created"));
    }

}


