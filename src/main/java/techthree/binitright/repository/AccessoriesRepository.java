package techthree.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techthree.binitright.model.Accessories;

@Repository
public interface AccessoriesRepository extends JpaRepository<Accessories, Long> {

}