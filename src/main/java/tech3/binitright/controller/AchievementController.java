package tech3.binitright.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.request.AchievementDTO;
import tech3.binitright.service.AchievementImplementation;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public final class AchievementController {

    private final AchievementImplementation achievementImplementation;

    public AchievementController(final AchievementImplementation achievementImplementation) {
        this.achievementImplementation = achievementImplementation;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AchievementDTO>> getUserAchievements(@PathVariable final Long userId) {
        final List<AchievementDTO> list = achievementImplementation.getAchievementsForUser(userId);
        return ResponseEntity.ok(list);
    }
}