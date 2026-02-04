package tech3.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.model.UserAccessories;
import tech3.binitright.repository.UserAccessoriesRepository;

@Service
public class UserAccessoriesImplementation implements UserAccessoriesInterface {

    @Autowired
    private UserAccessoriesRepository userAccessoriesRepository;

    @Override
    public void save(UserAccessories userAccessories) {
        userAccessoriesRepository.save(userAccessories);
    }

}