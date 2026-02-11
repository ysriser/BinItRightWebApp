package techthree.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import techthree.binitright.model.DropOffLocation;

import java.util.List;
import java.util.Optional;

public interface DropOffLocationRepository extends JpaRepository<DropOffLocation, String> {
    List<DropOffLocation> findAll();

    Optional<DropOffLocation> findById(String id);

}
