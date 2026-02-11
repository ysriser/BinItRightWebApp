package techthree.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techthree.binitright.interfacemethods.AccessoriesInterface;
import techthree.binitright.model.Accessories;
import techthree.binitright.repository.AccessoriesRepository;

import java.util.List;

@Service
public class AccessoriesImplementation implements AccessoriesInterface {

    @Autowired
    private AccessoriesRepository accessoriesRepository;

    @Override
    public List<Accessories> findAll() {
        return accessoriesRepository.findAll();
    }

}