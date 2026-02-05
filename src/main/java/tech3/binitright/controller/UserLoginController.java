package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;
import tech3.binitright.request.LoginRequest;
import tech3.binitright.request.ShareAchievementRequest;
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

        if (request.getUsername() == null || request.getPassword() == null) {
            return new LoginResponse(false, "Missing credentials", null, null, null);
        }

        List<User> users = userService.findByUsername(request.getUsername());

        if (users.isEmpty()) {
            return new LoginResponse(false, "Invalid username or password", null, null, null);
        }

        User user = users.get(0);

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword_hash()
        )) {
            return new LoginResponse(false, "Invalid username or password", null, null, null);
        }

        String token = jwtUtil.generateToken(user);
        
        return new LoginResponse(true, "Login success", token, user.getId(), user.getUsername());
    }

    @GetMapping("/profile")
    public ResponseEntity<ShareAchievementRequest> getUserProfile(@AuthenticationPrincipal User user) {
        ShareAchievementRequest response = new ShareAchievementRequest(
            user.getId(),
            user.getUsername(),
            user.getEmailAddress(),
            user.getCurrentRank()
        );
        return ResponseEntity.ok(response);
    }
}