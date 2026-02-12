package techthree.binitright.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techthree.binitright.interfacemethods.UserInterface;
import techthree.binitright.model.User;
import techthree.binitright.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserImplementation implements UserInterface {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AchievementImplementation achievementImplementation;

    @Override
    public User saveUser(User user) {
        userRepo.save(user);
        achievementImplementation.checkProfileAchievements(user);
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

    public boolean existsByEmailAddress(String emailAddress) {
        return userRepo.existsByEmailAddress(emailAddress);
    }
}