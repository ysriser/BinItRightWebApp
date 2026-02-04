package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;
import tech3.binitright.request.LoginRequest;
import tech3.binitright.response.LoginResponse;
import tech3.binitright.util.JwtUtil;

import java.util.List;

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
    public LoginResponse login(@RequestBody LoginRequest request) {

        // ✅ 1. Validate request body
        if (request.getUsername() == null || request.getPassword() == null) {
            return new LoginResponse(false, "Missing credentials", null, null);
        }

        // ✅ 2. Find APP USER (app_users table)
        List<User> users = userService.findByUsername(request.getUsername());

        if (users.isEmpty()) {
            return new LoginResponse(false, "Invalid username or password", null, null);
        }

        User user = users.get(0);

        // ✅ 3. Match BCrypt password against password_hash
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword_hash()
        )) {
            return new LoginResponse(false, "Invalid username or password", null, null);
        }

        // ✅ 4. Generate JWT token
        String token = jwtUtil.generateToken(user);

        // ✅ 5. Return success
        return new LoginResponse(true, "Login success", token, user.getId());
    }
}


