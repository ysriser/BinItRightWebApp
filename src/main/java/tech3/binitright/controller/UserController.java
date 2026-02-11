package tech3.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;
import tech3.binitright.response.UserProfileResponse;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserInterface userService;

    public UserController(UserInterface userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(new UserProfileResponse(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
