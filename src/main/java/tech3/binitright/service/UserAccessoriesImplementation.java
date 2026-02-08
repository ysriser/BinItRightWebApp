package tech3.binitright.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.repository.UserAccessoriesRepository;

@Service
public final class UserAccessoriesImplementation implements UserAccessoriesInterface {

    @Autowired
    private UserAccessoriesRepository userAccessoriesRepository;

    @Override
    public void save(final UserAccessories userAccessories) {
        userAccessoriesRepository.save(userAccessories);
    }

    @Override
    public List<UserAccessories> findAll() {
        return List.of();
    }

    @Override
    public List<UserAccessories> findAllByUserUId(final Long id) {
        return userAccessoriesRepository.findAllByUserUId(id);
    }

    @Override
    @Transactional
    public void equipItem(final Long userId, final Long accessoriesId) {

        final List<UserAccessories> currentEquipped =
                userAccessoriesRepository.findByUserUIdAndEquippedTrue(userId);

        for (final UserAccessories item : currentEquipped) {
            item.setEquipped(false);
        }
        userAccessoriesRepository.saveAll(currentEquipped);

        final UserAccessories itemToEquip =
                userAccessoriesRepository.findByUserUIdAndAccessoriesUAccessoriesId(userId, accessoriesId);

        if (itemToEquip == null) {
            throw new RuntimeException("Accessory not owned");
        }

        itemToEquip.setEquipped(true);
        userAccessoriesRepository.save(itemToEquip);
    }

    @Override
    @Transactional
    public void unequipItem(final Long userId, final Long accessoryId) {
        final UserAccessories itemToUnequip =
                userAccessoriesRepository.findByUserUIdAndAccessoriesUAccessoriesId(userId, accessoryId);

        if (itemToUnequip != null) {
            itemToUnequip.setEquipped(false);
            userAccessoriesRepository.save(itemToUnequip);
        }
    }
}