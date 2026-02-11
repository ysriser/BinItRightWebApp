package techthree.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import techthree.binitright.model.DropOffLocation;

public interface LocationRepository extends JpaRepository<DropOffLocation, String>{

}
