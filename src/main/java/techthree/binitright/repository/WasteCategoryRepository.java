package techthree.binitright.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import techthree.binitright.model.WasteCategories;

public interface WasteCategoryRepository extends JpaRepository<WasteCategories, Long>{
	public Optional<WasteCategories> findByNameIgnoreCase(String name);

}
