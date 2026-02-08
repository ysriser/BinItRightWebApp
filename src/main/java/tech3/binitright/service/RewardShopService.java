package tech3.binitright.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tech3.binitright.dto.ShopItemDTO;
import tech3.binitright.model.Accessories;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.repository.AccessoriesRepository;
import tech3.binitright.repository.UserAccessoriesRepository;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.response.RedeemResponse;

@Service
public final class RewardShopService {
    private final UserRepository userRepository;
    private final AccessoriesRepository accessoryRepository;
    private final UserAccessoriesRepository userAccessoriesRepository;
    private final AchievementImplementation achievementImplementation;

    public RewardShopService(
            final UserRepository userRepository,
            final AccessoriesRepository accessoryRepository,
            final UserAccessoriesRepository userAccessoriesRepository,
            final AchievementImplementation achievementImplementation
    ) {
        this.userRepository = userRepository;
        this.accessoryRepository = accessoryRepository;
        this.userAccessoriesRepository = userAccessoriesRepository;
        this.achievementImplementation = achievementImplementation;
    }

    public List<Accessories> getItems() {
        return accessoryRepository.findAll();
    }

    public List<ShopItemDTO> getItemsForUser(final Long userId) {

        final List<Accessories> all = accessoryRepository.findAll();
        final List<UserAccessories> ownedRows = userAccessoriesRepository.findAllByUserUId(userId);

        final java.util.Map<Long, Boolean> ownedEquippedMap = new java.util.HashMap<>();
        for (final UserAccessories ua : ownedRows) {
            ownedEquippedMap.put(ua.getAccessories().getAccessoriesId(), ua.isEquipped());
        }

        final List<ShopItemDTO> result = new java.util.ArrayList<>();
        for (final Accessories a : all) {
            final Long id = a.getAccessoriesId();
            final boolean owned = ownedEquippedMap.containsKey(id);
            final boolean equipped = owned && Boolean.TRUE.equals(ownedEquippedMap.get(id));

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
    public RedeemResponse redeem(final Long userId, final Long accessoriesId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final Accessories accessory = accessoryRepository.findById(accessoriesId)
                .orElseThrow(() -> new RuntimeException("Accessory not found"));

        final boolean alreadyOwned =
                userAccessoriesRepository
                        .existsByUserUIdAndAccessoriesUAccessoriesId(userId, accessoriesId);

        final int balance = user.getPointBalance() == null ? 0 : user.getPointBalance();
        final int price = accessory.getRequiredPoints();

        if (alreadyOwned) {
            return new RedeemResponse(balance, "Already owned");
        }

        if (balance < price) {
            return new RedeemResponse(balance, "Not enough points");
        }

        user.setPointBalance(balance - price);
        userRepository.save(user);

        final UserAccessories ua = new UserAccessories();
        ua.setUser(user);
        ua.setAccessories(accessory);
        ua.setEquipped(false);
        userAccessoriesRepository.save(ua);

        achievementImplementation.unlockAchievement(userId, 10L);
        achievementImplementation.checkProfileAchievements(user);

        return new RedeemResponse(user.getPointBalance(), "Redeemed successfully");
    }

}