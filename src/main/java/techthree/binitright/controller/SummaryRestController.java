package techthree.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techthree.binitright.dto.UserProfileDTO;
import techthree.binitright.interfacemethods.ChatInterface;
import techthree.binitright.interfacemethods.CheckInInterface;
import techthree.binitright.interfacemethods.UserAccessoriesInterface;
import techthree.binitright.interfacemethods.UserInterface;
import techthree.binitright.model.User;
import techthree.binitright.model.UserAccessories;
import techthree.binitright.service.*;

import java.math.BigDecimal;

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
    private ChatInterface chatService;

    @Autowired
    public void setChatService(ChatImplementation chatInserviceImp) {
        this.chatService = chatInserviceImp;
    }

    @Autowired
    private UserAccessoriesInterface userAccessoriesService;

    @Autowired
    private AchievementImplementation achievementService;

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

//        String aiSummary = chatService.generateProgressSummary(
//                user.getPointBalance(),
//                user.getCarbonEmissionSaved(),
//                user.getCurrentRank(),
//                totalRecycled
//        );

        String aiSummary = "You're making a real environmental impact 🌱 Keep recycling to climb higher and save more CO₂!";

        int totalAchievements = achievementService.getTotalAchievements(userId);
        BigDecimal co2SavedBD = emissionService.getUserTotalCo2Saved(userId);
        float co2Saved = co2SavedBD == null ? 0f : co2SavedBD.floatValue();

        UserProfileDTO dto = new UserProfileDTO(
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
