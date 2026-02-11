package tech3.binitright.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.dto.LeaderboardDTO;
import tech3.binitright.interfacemethods.CheckInInterface;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final CheckInInterface checkInService;

    public LeaderboardController(CheckInInterface checkInService) {
        this.checkInService = checkInService;
    }

    @GetMapping
    public ResponseEntity<List<LeaderboardDTO>> getMonthlyLeaderboard() {
        final List<LeaderboardDTO> leaderboard = checkInService.getMonthlyLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
}