package techthree.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import techthree.binitright.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmailAddress(String emailAddress);
}
