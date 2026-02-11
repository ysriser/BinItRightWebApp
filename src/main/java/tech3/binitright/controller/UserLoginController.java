package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.request.RegisterRequest;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;
import tech3.binitright.request.LoginRequest;
import tech3.binitright.response.LoginResponse;
import tech3.binitright.response.RegisterResponse;
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
            return new LoginResponse(false, "Missing credentials", null);
        }


        List<User> users = userService.findByUsername(request.getUsername());

        if (users.isEmpty()) {
            return new LoginResponse(false, "Invalid username or password", null);
        }

        User user = users.get(0);


        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword_hash()
        )) {
            return new LoginResponse(false, "Invalid username or password", null);
        }


        String token = jwtUtil.generateToken(user);

        return new LoginResponse(true, "Login success", token);
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req) {

        if (userService.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse(false, "Username already exists"));
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword_hash(passwordEncoder.encode(req.getPassword()));
        user.setRole("USER");
        userService.saveUser(user);

        return ResponseEntity.ok(new RegisterResponse(true, "Account created"));
    }

}


