package tech3.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.dto.ShopItemDTO;
import tech3.binitright.response.RedeemResponse;
import tech3.binitright.service.RewardShopService;
import tech3.binitright.util.JwtUtil;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/reward-shop")
@CrossOrigin(origins = "*")
public class RewardShopController {
    private final RewardShopService rewardShopService;

    public RewardShopController(
            RewardShopService rewardShopService
    ) {
        this.rewardShopService = rewardShopService;
    }


    @GetMapping("/items")
    public ResponseEntity<List<ShopItemDTO>> getShopItems(Authentication authentication) {

        String userIdStr = (String) authentication.getPrincipal(); // (or authentication.getName())
        Long userId = Long.valueOf(userIdStr);

        return ResponseEntity.ok(rewardShopService.getItemsForUser(userId));
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