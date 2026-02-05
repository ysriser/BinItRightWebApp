package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech3.binitright.model.Accessories;

import java.util.List;

@Repository
public interface AccessoriesRepository extends JpaRepository<Accessories, Long> {

}