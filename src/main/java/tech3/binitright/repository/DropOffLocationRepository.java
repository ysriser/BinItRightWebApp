package tech3.binitright.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tech3.binitright.model.DropOffLocation;

public interface DropOffLocationRepository extends JpaRepository<DropOffLocation, String> {
    @Override
	List<DropOffLocation> findAll();

    @Override
	Optional<DropOffLocation> findById(String id);

}
