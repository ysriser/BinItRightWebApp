package techthree.binitright.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import techthree.binitright.response.RedeemResponse;
import techthree.binitright.model.Accessories;
import techthree.binitright.model.User;
import techthree.binitright.model.UserAccessories;
import techthree.binitright.repository.AccessoriesRepository;
import techthree.binitright.repository.UserAccessoriesRepository;
import techthree.binitright.repository.UserRepository;

import java.util.List;

@Service
public class RedeemService {

    private final UserRepository userRepo;
    private final AccessoriesRepository accessoryRepo;
    private final UserAccessoriesRepository userAccessoryRepo;

    public RedeemService(UserRepository userRepo,
                         AccessoriesRepository accessoryRepo,
                         UserAccessoriesRepository userAccessoryRepo) {
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
    public RedeemResponse redeem(Long userId, Long accessoriesId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Accessories accessory = accessoryRepo.findById(accessoriesId)
                .orElseThrow(() -> new RuntimeException("Accessory not found"));

        boolean alreadyOwned =
                userAccessoryRepo.existsByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId);

        int balance = (user.getPointBalance() == null) ? 0 : user.getPointBalance();
        int price = accessory.getRequiredPoints();

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
        UserAccessories ua = new UserAccessories();
        ua.setUser(user);
        ua.setAccessories(accessory);
        ua.setEquipped(false);
        userAccessoryRepo.save(ua);

        return new RedeemResponse(user.getPointBalance(), "Redeemed successfully");
    }
}
