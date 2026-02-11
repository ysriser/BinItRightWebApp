package techthree.binitright.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import techthree.binitright.dto.LeaderboardDTO;
import techthree.binitright.interfacemethods.CheckInInterface;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private CheckInInterface checkInService;

    public LeaderboardController(CheckInInterface checkInService) {
        this.checkInService = checkInService;
    }

    @GetMapping
    public ResponseEntity<List<LeaderboardDTO>> getMonthlyLeaderboard() {
        final List<LeaderboardDTO> leaderboard = checkInService.getMonthlyLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
}