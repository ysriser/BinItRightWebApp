package techthree.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techthree.binitright.request.AchievementDTO;
import techthree.binitright.service.AchievementImplementation;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public class AchievementController {

    private final AchievementImplementation achievementImplementation;

    public AchievementController(AchievementImplementation achievementImplementation) {
        this.achievementImplementation = achievementImplementation;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AchievementDTO>> getUserAchievements(@PathVariable Long userId) {
        List<AchievementDTO> list = achievementImplementation.getAchievementsForUser(userId);
        return ResponseEntity.ok(list);
    }
}