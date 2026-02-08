package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech3.binitright.dto.UserProfileDTO;
import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.service.CheckInImplementation;
import tech3.binitright.service.EmissionService;
import tech3.binitright.service.UserAccessoriesImplementation;
import tech3.binitright.service.UserImplementation;

@RestController
@RequestMapping("/api/summary")
public class SummaryRestController {

    @Autowired
    private EmissionService emissionService;
    @Autowired
    private UserInterface userService;

    @Autowired
    public void setUserService(UserImplementation userImplementation) {
        this.userService = userImplementation;
    }

    @Autowired
    private CheckInInterface checkInService;

    public void setcheckInService(CheckInImplementation checkInserviceImp) {
        this.checkInService = checkInserviceImp;
    }

    @Autowired
    private UserAccessoriesInterface userAccessoriesService;

    @Autowired
    public void setUserAccessoriesService(UserAccessoriesImplementation userAccessoriesImplementation) {
        this.userAccessoriesService = userAccessoriesImplementation;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfileSummary(Authentication authentication) {
        // 1. Safety check for the "User1" string issue we saw earlier
        Long userId;
        try {
            userId = Long.valueOf(authentication.getName());
        } catch (NumberFormatException e) {
            // Fallback logic if token has username instead of ID
            User u = userService.findByUsername(authentication.getName()).get(0);
            userId = u.getId();
        }

        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        String avatarName = userAccessoriesService.findAllByUser_Id(userId)
                .stream()
                .filter(UserAccessories::isEquipped)
                .findFirst()
                .map(ua -> ua.getAccessories().getName())
                .orElse("default_avatar");

        Integer totalRecycled = checkInService.getUserTotalRecycled(userId);

        UserProfileDTO dto = new UserProfileDTO(
                user.getName(),
                user.getPointBalance(),
                avatarName,
                totalRecycled
        );

        return ResponseEntity.ok(dto);
    }
}
