package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech3.binitright.model.UserAccessories;

import java.util.List;

public interface UserAccessoriesRepository extends JpaRepository<UserAccessories, Long>{

}
