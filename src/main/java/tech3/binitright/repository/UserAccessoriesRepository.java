package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech3.binitright.model.UserAccessories;

import java.util.List;

public interface UserAccessoriesRepository extends JpaRepository<UserAccessories, Long>{

    List<UserAccessories> findAllByUserUId(Long id);

    List<UserAccessories> findByUserUIdAndEquippedTrue(Long userId);

    UserAccessories findByUserUIdAndAccessoriesUAccessoriesId(Long userId, Long accessoriesId);
    
    boolean existsByUserUIdAndAccessoriesUAccessoriesId(Long userId, Long accessoriesId);
}
