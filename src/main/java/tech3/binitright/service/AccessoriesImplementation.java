package tech3.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech3.binitright.interfacemethods.AccessoriesInterface;
import tech3.binitright.interfacemethods.NewsInterface;
import tech3.binitright.model.Accessories;
import tech3.binitright.model.News;
import tech3.binitright.repository.AccessoriesRepository;
import tech3.binitright.repository.NewsRepository;

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