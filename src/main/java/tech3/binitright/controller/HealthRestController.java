package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.config.AchievementSeeder;

@RestController
public class HealthRestController {

    @Autowired
    private AchievementSeeder achievementSeeder; // The class that runs your API-based seeding

    @GetMapping("/api/ready")
    public ResponseEntity<String> checkReadiness() {
        if (achievementSeeder.isSeedingComplete()) {
            return ResponseEntity.ok("READY");
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("SEEDINGUINUPROGRESS");
        }
    }
}
