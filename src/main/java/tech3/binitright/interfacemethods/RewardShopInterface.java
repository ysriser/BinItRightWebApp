package tech3.binitright.interfacemethods;

import tech3.binitright.dto.ShopItemDTO;
import tech3.binitright.response.RedeemResponse;

import java.util.List;

public interface RewardShopInterface {
    List<ShopItemDTO> getItemsForUser(Long userId);
    RedeemResponse redeem(Long userId, Long accessoriesId);
}
