package tech3.binitright.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech3.binitright.interfacemethods.AccessoriesInterface;
import tech3.binitright.model.Accessories;
import tech3.binitright.repository.AccessoriesRepository;

@Service
public final class AccessoriesImplementation implements AccessoriesInterface {

    @Autowired
    private AccessoriesRepository accessoriesRepository;

    @Override
    public List<Accessories> findAll() {
        return accessoriesRepository.findAll();
    }

}