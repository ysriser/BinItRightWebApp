package tech3.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.dto.RedeemResponse;
import tech3.binitright.model.Accessories;
import tech3.binitright.service.RewardShopService;
import tech3.binitright.util.JwtUtil;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/reward-shop")
@CrossOrigin(origins = "*")
public class RewardShopController {
    private final RewardShopService rewardShopService;
    private final JwtUtil jwtUtil;

    public RewardShopController(
            RewardShopService rewardShopService,
            JwtUtil jwtUtil
    ) {
        this.rewardShopService = rewardShopService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/items")
    public ResponseEntity<List<Accessories>> getItems() {
        return ResponseEntity.ok(rewardShopService.getItems());
    }

    @PostMapping("/redeem/{accessoriesId}")
    public ResponseEntity<RedeemResponse> redeem(
            @PathVariable Long accessoriesId,
            Authentication authentication
    ) {
        String userIdStr = (String) authentication.getPrincipal();
        Long userId = Long.valueOf(userIdStr);

        RedeemResponse response =
                rewardShopService.redeem(userId, accessoriesId);

        return ResponseEntity.ok(response);
    }

}