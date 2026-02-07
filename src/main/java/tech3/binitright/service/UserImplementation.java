package tech3.binitright.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;
import tech3.binitright.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserImplementation implements UserInterface {

    @Autowired
    private UserRepository userRepo;

    @Override
    public User saveUser(User user) {
        userRepo.save(user);
        return user;
    }
    @Override
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public List<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User findById(Long userId) {
        return userRepo.findById(userId).orElse(null);
    }
}
