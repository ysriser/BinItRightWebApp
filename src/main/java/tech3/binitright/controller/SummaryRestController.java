package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.dto.UserProfileDTO;
import tech3.binitright.interfacemethods.ChatInterface;
import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.service.AchievementImplementation;
import tech3.binitright.service.ChatImplementation;
import tech3.binitright.service.CheckInImplementation;
import tech3.binitright.service.UserAccessoriesImplementation;
import tech3.binitright.service.UserImplementation;

@RestController
@RequestMapping("/api/summary")
public final class SummaryRestController {

    @Autowired
    private UserInterface userService;

    @Autowired
    public void setUserService(final UserImplementation userImplementation) {
        this.userService = userImplementation;
    }

    @Autowired
    private CheckInInterface checkInService;

    public void setcheckInService(final CheckInImplementation checkInserviceImp) {
        this.checkInService = checkInserviceImp;
    }

    @Autowired
    private ChatInterface chatService;

    @Autowired
    public void setChatService(final ChatImplementation chatInserviceImp) {
        this.chatService = chatInserviceImp;
    }

    @Autowired
    private UserAccessoriesInterface userAccessoriesService;

    @Autowired
    private AchievementImplementation achievementService;

    @Autowired
    public void setUserAccessoriesService(final UserAccessoriesImplementation userAccessoriesImplementation) {
        this.userAccessoriesService = userAccessoriesImplementation;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfileSummary(final Authentication authentication) {
        Long userId;
        try {
            userId = Long.valueOf(authentication.getName());
        } catch (final NumberFormatException e) {
            final User u = userService.findByUsername(authentication.getName()).get(0);
            userId = u.getId();
        }

        final User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        final String avatarName = userAccessoriesService.findAllByUserUId(userId)
                .stream()
                .filter(UserAccessories::isEquipped)
                .findFirst()
                .map(ua -> ua.getAccessories().getName())
                .orElse("defaultUavatar");

        final Integer totalRecycled = checkInService.getUserTotalRecycled(userId);
        final String aiSummary = "You're making a real environmental impact 🌱"
                + " Keep recycling to climb higher and save more CO₂!";

        final int totalAchievements = achievementService.getTotalAchievements(userId);
        final float co2Saved = user.getCarbonEmissionSaved();

        final UserProfileDTO dto = new UserProfileDTO(
                user.getName(),
                user.getPointBalance(),
                avatarName,
                totalRecycled,
                aiSummary,
                totalAchievements,
                co2Saved
        );

        return ResponseEntity.ok(dto);
    }
}