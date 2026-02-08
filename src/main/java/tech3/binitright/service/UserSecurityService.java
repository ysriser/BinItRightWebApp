package tech3.binitright.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;

@Service
public class UserSecurityService {

    @Autowired
    private UserInterface userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    // Validate user login
    public boolean validateUser(final String username, final String rawPassword) {
        final List<User> users = userService.findByUsername(username);
        if (users == null || users.isEmpty()) {
			return false;
		}

        final User user = users.get(0);
        return passwordEncoder.matches(rawPassword, user.getPasswordUhash());
    }

    public String generateToken() {
        final byte[] bytes = new byte[32]; // 256-bit token
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Save user with hashed password
    public void saveUser(final User user) {
        final String hash = passwordEncoder.encode(user.getPasswordUhash());
        user.setPasswordUhash(hash);
        userService.saveUser(user);
    }
}
