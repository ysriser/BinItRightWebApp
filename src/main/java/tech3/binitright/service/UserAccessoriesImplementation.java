package tech3.binitright.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.repository.UserAccessoriesRepository;
import tech3.binitright.request.RedeemRequest;

import java.util.List;

@Service
public class UserAccessoriesImplementation implements UserAccessoriesInterface {

    @Autowired
    private UserAccessoriesRepository userAccessoriesRepository;

    @Override
    public void save(UserAccessories userAccessories) {
        userAccessoriesRepository.save(userAccessories);
    }

    @Override
    public List<UserAccessories> findAll() {
        return List.of();
    }

    @Override
    public List<UserAccessories> findAllByUserUId(Long id) {
        return userAccessoriesRepository.findAllByUserUId(id);
    }

    @Override
    @Transactional
    public void equipItem(Long userId, Long accessoriesId) {

        List<UserAccessories> currentEquipped =
                userAccessoriesRepository.findByUserUIdAndEquippedTrue(userId);

        for (UserAccessories item : currentEquipped) {
            item.setEquipped(false);
        }
        userAccessoriesRepository.saveAll(currentEquipped);

        UserAccessories itemToEquip =
                userAccessoriesRepository.findByUserUIdAndAccessoriesUAccessoriesId(userId, accessoriesId);

        if (itemToEquip == null) {
            throw new RuntimeException("Accessory not owned");
        }

        itemToEquip.setEquipped(true);
        userAccessoriesRepository.save(itemToEquip);
    }

    @Override
    @Transactional
    public void unequipItem(Long userId, Long accessoryId) {
        UserAccessories itemToUnequip =
                userAccessoriesRepository.findByUserUIdAndAccessoriesUAccessoriesId(userId, accessoryId);

        if (itemToUnequip != null) {
            itemToUnequip.setEquipped(false);
            userAccessoriesRepository.save(itemToUnequip);
        }
    }
}