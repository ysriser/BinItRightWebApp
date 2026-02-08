package tech3.binitright.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tech3.binitright.model.DropOffLocation;
import tech3.binitright.model.WasteCategories;

public interface WasteCategoryRepository extends JpaRepository<WasteCategories, Long>{
	public Optional<WasteCategories> findByNameIgnoreCase(String name);

}
