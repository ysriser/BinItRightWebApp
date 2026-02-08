package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech3.binitright.model.DropOffLocation;

public interface LocationRepository extends JpaRepository<DropOffLocation, String>{

}
