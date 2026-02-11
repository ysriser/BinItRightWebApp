package techthree.binitright.interfacemethods;

import techthree.binitright.model.UserAccessories;

import java.util.List;

public interface UserAccessoriesInterface {

    void save(UserAccessories userAccessories);

    List<UserAccessories> findAll();

    List<UserAccessories> findAllByUser_Id(Long id);

    void equipItem(Long userId, Long accessoriesId);

    void unequipItem(Long id, Long accessoriesId);
}
