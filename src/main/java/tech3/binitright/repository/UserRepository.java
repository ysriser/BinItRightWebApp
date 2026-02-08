package tech3.binitright.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech3.binitright.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
