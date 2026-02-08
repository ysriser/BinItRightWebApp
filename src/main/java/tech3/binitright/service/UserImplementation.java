package tech3.binitright.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.User;
import tech3.binitright.repository.UserRepository;

@Service
@Transactional
public final class UserImplementation implements UserInterface {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AchievementImplementation achievementImplementation;

    @Override
    public User saveUser(final User user) {
        userRepo.save(user);
        achievementImplementation.checkProfileAchievements(user);
        return user;
    }
    @Override
    public boolean existsByUsername(final String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public List<User> findByUsername(final String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User findById(final Long userId) {
        return userRepo.findById(userId).orElse(null);
    }
}