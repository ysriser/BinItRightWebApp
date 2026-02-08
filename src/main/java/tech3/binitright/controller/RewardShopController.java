package tech3.binitright.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.dto.ShopItemDTO;
import tech3.binitright.response.RedeemResponse;
import tech3.binitright.service.RewardShopService;
import tech3.binitright.util.JwtUtil;

@RestController
@RequestMapping("/api/reward-shop")
@CrossOrigin(origins = "*")
public class RewardShopController {
    private final RewardShopService rewardShopService;
    private final JwtUtil jwtUtil;

    public RewardShopController(
            final RewardShopService rewardShopService,
            final JwtUtil jwtUtil
    ) {
        this.rewardShopService = rewardShopService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("/items")
    public ResponseEntity<List<ShopItemDTO>> getShopItems(final Authentication authentication) {

        final String userIdStr = (String) authentication.getPrincipal(); // (or authentication.getName())
        final Long userId = Long.valueOf(userIdStr);

        return ResponseEntity.ok(rewardShopService.getItemsForUser(userId));
    }

    @PostMapping("/redeem/{accessoriesId}")
    public ResponseEntity<RedeemResponse> redeem(
            @PathVariable final Long accessoriesId,
            final Authentication authentication
    ) {
        final String userIdStr = (String) authentication.getPrincipal();
        final Long userId = Long.valueOf(userIdStr);

        final RedeemResponse response =
                rewardShopService.redeem(userId, accessoriesId);

        return ResponseEntity.ok(response);
    }

}