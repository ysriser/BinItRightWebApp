package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech3.binitright.model.CheckIn;
import tech3.binitright.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
