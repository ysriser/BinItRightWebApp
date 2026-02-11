package techthree.binitright.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techthree.binitright.interfacemethods.UserAccessoriesInterface;
import techthree.binitright.model.UserAccessories;
import techthree.binitright.repository.UserAccessoriesRepository;

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
    public List<UserAccessories> findAllByUser_Id(Long id) {
        return userAccessoriesRepository.findAllByUser_Id(id);
    }

    @Override
    @Transactional
    public void equipItem(Long userId, Long accessoriesId) {

        List<UserAccessories> currentEquipped =
                userAccessoriesRepository.findByUser_IdAndEquippedTrue(userId);

        for (UserAccessories item : currentEquipped) {
            item.setEquipped(false);
        }
        userAccessoriesRepository.saveAll(currentEquipped);

        UserAccessories itemToEquip =
                userAccessoriesRepository.findByUser_IdAndAccessories_AccessoriesId(userId, accessoriesId);

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
                userAccessoriesRepository.findByUser_IdAndAccessories_AccessoriesId(userId, accessoryId);

        if (itemToUnequip != null) {
            itemToUnequip.setEquipped(false);
            userAccessoriesRepository.save(itemToUnequip);
        }
    }
}