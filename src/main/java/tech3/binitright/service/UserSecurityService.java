package tech3.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class UserSecurityService {

    @Autowired
    private UserInterface userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    // Validate user login
    public boolean validateUser(String username, String rawPassword) {
        List<User> users = userService.findByUsername(username);
        if (users == null || users.isEmpty()) return false;

        User user = users.get(0);
        return passwordEncoder.matches(rawPassword, user.getPassword_hash());
    }

    public String generateToken() {
        byte[] bytes = new byte[32]; // 256-bit token
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Save user with hashed password
    public void saveUser(User user) {
        String hash = passwordEncoder.encode(user.getPassword_hash());
        user.setPassword_hash(hash);
        userService.saveUser(user);
    }
}
