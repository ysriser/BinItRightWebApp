package tech3.binitright.interfacemethods;

import java.util.List;

import tech3.binitright.model.UserAccessories;

public interface UserAccessoriesInterface {

    void save(UserAccessories userAccessories);

    List<UserAccessories> findAll();

    List<UserAccessories> findAllByUserUId(Long id);

    void equipItem(Long userId, Long accessoriesId);

    void unequipItem(Long id, Long accessoriesId);
}
