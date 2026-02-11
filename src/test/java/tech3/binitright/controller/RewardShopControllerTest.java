package tech3.binitright.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import tech3.binitright.dto.ShopItemDTO;
import tech3.binitright.interfacemethods.RewardShopInterface;
import tech3.binitright.response.RedeemResponse;
import tech3.binitright.service.RewardShopService;
import tech3.binitright.util.JwtUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class RewardShopControllerTest {
    static class FakeRewardShopService implements RewardShopInterface {
        Long lastUserId;
        Long lastAccessoriesId;

        @Override
        public List<ShopItemDTO> getItemsForUser(Long userId) {
            lastUserId = userId;
            return List.of(
                    new ShopItemDTO(1L, "ItemA", 10, false, false),
                    new ShopItemDTO(2L, "ItemB", 20, true, true)
            );
        }

        @Override
        public RedeemResponse redeem(Long userId, Long accessoriesId) {
            lastUserId = userId;
            lastAccessoriesId = accessoriesId;
            return new RedeemResponse(100, "Redeemed successfully");
        }
    }

    @Test
    void getShopItemsReturnsServiceData() {
        FakeRewardShopService fakeService = new FakeRewardShopService();
        JwtUtil jwtUtil = null;

        RewardShopController controller = new RewardShopController(fakeService);

        Authentication auth = new UsernamePasswordAuthenticationToken("5", null, List.of());

        ResponseEntity<List<ShopItemDTO>> response = controller.getShopItems(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(5L, fakeService.lastUserId);
    }

    @Test
    void redeemReturnsRedeemResponse() {
        FakeRewardShopService fakeService = new FakeRewardShopService();
        JwtUtil jwtUtil = null;

        RewardShopController controller = new RewardShopController(fakeService);

        Authentication auth = new UsernamePasswordAuthenticationToken("7", null, List.of());

        ResponseEntity<RedeemResponse> response = controller.redeem(99L, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Redeemed successfully", response.getBody().getMessage());
        assertEquals(7L, fakeService.lastUserId);
        assertEquals(99L, fakeService.lastAccessoriesId);
    }
}