package tech3.binitright.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tech3.binitright.dto.ShopItemDTO;
import tech3.binitright.interfacemethods.RewardShopInterface;
import tech3.binitright.response.RedeemResponse;
import tech3.binitright.model.Accessories;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.repository.AccessoriesRepository;
import tech3.binitright.repository.UserAccessoriesRepository;
import tech3.binitright.repository.UserRepository;

import java.util.List;

@Service
public class RewardShopService implements RewardShopInterface {
    private final UserRepository userRepository;
    private final AccessoriesRepository accessoryRepository;
    private final UserAccessoriesRepository userAccessoriesRepository;
    private final AchievementImplementation achievementImplementation;

    public RewardShopService(
            UserRepository userRepository,
            AccessoriesRepository accessoryRepository,
            UserAccessoriesRepository userAccessoriesRepository,
            AchievementImplementation achievementImplementation
    ) {
        this.userRepository = userRepository;
        this.accessoryRepository = accessoryRepository;
        this.userAccessoriesRepository = userAccessoriesRepository;
        this.achievementImplementation = achievementImplementation;
    }

    public List<Accessories> getItems() {
        return accessoryRepository.findAll();
    }

    public List<ShopItemDTO> getItemsForUser(Long userId) {

        List<Accessories> all = accessoryRepository.findAll();
        List<UserAccessories> ownedRows = userAccessoriesRepository.findAllByUser_Id(userId);

        java.util.Map<Long, Boolean> ownedEquippedMap = new java.util.HashMap<>();
        for (UserAccessories ua : ownedRows) {
            ownedEquippedMap.put(ua.getAccessories().getAccessoriesId(), ua.isEquipped());
        }

        List<ShopItemDTO> result = new java.util.ArrayList<>();
        for (Accessories a : all) {
            Long id = a.getAccessoriesId();
            boolean owned = ownedEquippedMap.containsKey(id);
            boolean equipped = owned && Boolean.TRUE.equals(ownedEquippedMap.get(id));

            result.add(new ShopItemDTO(
                    id,
                    a.getName(),
                    a.getRequiredPoints(),
                    owned,
                    equipped
            ));
        }
        return result;
    }


    @Transactional
    public RedeemResponse redeem(Long userId, Long accessoriesId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Accessories accessory = accessoryRepository.findById(accessoriesId)
                .orElseThrow(() -> new RuntimeException("Accessory not found"));

        boolean alreadyOwned =
                userAccessoriesRepository
                        .existsByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId);

        int balance = user.getPointBalance() == null ? 0 : user.getPointBalance();
        int price = accessory.getRequiredPoints();

        if (alreadyOwned) {
            return new RedeemResponse(balance, "Already owned");
        }

        if (balance < price) {
            return new RedeemResponse(balance, "Not enough points");
        }

        user.setPointBalance(balance - price);
        userRepository.save(user);

        UserAccessories ua = new UserAccessories();
        ua.setUser(user);
        ua.setAccessories(accessory);
        ua.setEquipped(false);
        userAccessoriesRepository.save(ua);

        achievementImplementation.unlockAchievement(userId, 10L);
        achievementImplementation.checkProfileAchievements(user);

        return new RedeemResponse(user.getPointBalance(), "Redeemed successfully");
    }

}