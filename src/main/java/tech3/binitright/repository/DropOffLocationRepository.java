package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech3.binitright.model.DropOffLocation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DropOffLocationRepository extends JpaRepository<DropOffLocation, String> {
    List<DropOffLocation> findAll();

    Optional<DropOffLocation> findById(String id);

}
