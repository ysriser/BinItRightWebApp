package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech3.binitright.model.DropOffLocation;

import java.util.List;

public interface DropOffLocationRepository extends JpaRepository<DropOffLocation, Long> {
    List<DropOffLocation> findAll();
}
