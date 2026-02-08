package tech3.binitright.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tech3.binitright.model.Accessories;
import tech3.binitright.model.User;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.repository.AccessoriesRepository;
import tech3.binitright.repository.UserAccessoriesRepository;
import tech3.binitright.repository.UserRepository;
import tech3.binitright.response.RedeemResponse;

@Service
public final class RedeemService {

    private final UserRepository userRepo;
    private final AccessoriesRepository accessoryRepo;
    private final UserAccessoriesRepository userAccessoryRepo;

    public RedeemService(final UserRepository userRepo,
                         final AccessoriesRepository accessoryRepo,
                         final UserAccessoriesRepository userAccessoryRepo) {
        this.userRepo = userRepo;
        this.accessoryRepo = accessoryRepo;
        this.userAccessoryRepo = userAccessoryRepo;
    }

    // Get all shop items
    public List<Accessories> getItems() {
        return accessoryRepo.findAll();
    }

    // Redeem an accessory (deduct points + add to userAccessories)
    @Transactional
    public RedeemResponse redeem(final Long userId, final Long accessoriesId) {

        final User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final Accessories accessory = accessoryRepo.findById(accessoriesId)
                .orElseThrow(() -> new RuntimeException("Accessory not found"));

        final boolean alreadyOwned =
                userAccessoryRepo.existsByUserUIdAndAccessoriesUAccessoriesId(userId, accessoriesId);

        final int balance = (user.getPointBalance() == null) ? 0 : user.getPointBalance();
        final int price = accessory.getRequiredPoints();

        if (alreadyOwned) {
            return new RedeemResponse(balance, "Already owned");
        }

        if (balance < price) {
            return new RedeemResponse(balance, "Not enough points");
        }

        // Deduct points
        user.setPointBalance(balance - price);
        userRepo.save(user);

        // Save ownership
        final UserAccessories ua = new UserAccessories();
        ua.setUser(user);
        ua.setAccessories(accessory);
        ua.setEquipped(false);
        userAccessoryRepo.save(ua);

        return new RedeemResponse(user.getPointBalance(), "Redeemed successfully");
    }
}
