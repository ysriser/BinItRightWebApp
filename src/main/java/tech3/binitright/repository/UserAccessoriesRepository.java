package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech3.binitright.model.UserAccessories;

import java.util.List;

public interface UserAccessoriesRepository extends JpaRepository<UserAccessories, Long>{

    List<UserAccessories> findAllByUser_Id(Long id);

    List<UserAccessories> findByUser_IdAndEquippedTrue(Long userId);

    UserAccessories findByUser_IdAndAccessories_AccessoriesId(Long userId, Long accessoriesId);
    
    boolean existsByUser_IdAndAccessories_AccessoriesId(Long userId, Long accessoriesId);
}
