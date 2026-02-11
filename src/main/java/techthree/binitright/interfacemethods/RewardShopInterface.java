package techthree.binitright.interfacemethods;

import techthree.binitright.dto.ShopItemDTO;
import techthree.binitright.response.RedeemResponse;

import java.util.List;

public interface RewardShopInterface {
    List<ShopItemDTO> getItemsForUser(Long userId);
    RedeemResponse redeem(Long userId, Long accessoriesId);
}
