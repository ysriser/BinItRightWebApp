package techthree.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techthree.binitright.interfacemethods.UserInterface;
import techthree.binitright.model.User;
import techthree.binitright.response.UserProfileResponse;
import techthree.binitright.service.UserImplementation;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserInterface userService;

    public void setUserService(UserImplementation userserviceImp) {
        this.userService = userserviceImp;
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
