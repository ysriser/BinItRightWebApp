package tech3.binitright.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.repository.UserAccessoriesRepository;

import java.util.List;

@Service
public class UserAccessoriesImplementation implements UserAccessoriesInterface {

    @Autowired
    private UserAccessoriesRepository userAccessoriesRepository;

    @Override
    public void save(UserAccessories userAccessories) {
        userAccessoriesRepository.save(userAccessories);
    }

    public List<UserAccessories> findAll() {
        return userAccessoriesRepository.findAll();
    }

    @Override
    public List<UserAccessories> findAllByUser_Id(Long id) {
        return userAccessoriesRepository.findAllByUser_Id(id);
    }

    @Override
    @Transactional
    public void equipItem(Long userId, Long accessoriesId) {
        // 1. Optional: Unequip all other items first if only one avatar is allowed
        List<UserAccessories> currentEquipped = userAccessoriesRepository.findByUser_IdAndEquippedTrue(userId);
        for (UserAccessories item : currentEquipped) {
            item.setEquipped(false);
        }
        userAccessoriesRepository.saveAll(currentEquipped);

        // 2. Equip the selected item
        UserAccessories itemToEquip = userAccessoriesRepository.findByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId);
        if (itemToEquip != null) {
            itemToEquip.setEquipped(true);
            userAccessoriesRepository.save(itemToEquip);
        }
    }

    @Override
    @Transactional
    public void unequipItem(Long userId, Long accessoryId) {
        UserAccessories itemToUnequip = userAccessoriesRepository.findByUser_IdAndAccessories_AccessoriesId(userId, accessoryId);
        if (itemToUnequip != null) {
            itemToUnequip.setEquipped(false);
            userAccessoriesRepository.save(itemToUnequip);
        }
    }

}