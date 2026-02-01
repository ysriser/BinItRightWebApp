package tech3.binitright.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import tech3.binitright.model.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
	//List<CheckIn> findByStatus(Status status);
	
	@Query("""
		    SELECT ci
		    FROM CheckIn ci
		    JOIN FETCH ci.wasteCategories wc
		    JOIN FETCH ci.user u
		    WHERE ci.status = :status
		""")
		List<CheckIn> findByStatusWithDetails(@Param("status") CheckIn.Status status);

	@Query("SELECT c FROM CheckIn c " +
		       "JOIN FETCH c.user " +              
		       "JOIN FETCH c.wasteCategories " +         
		       "JOIN FETCH c.dropOffLocation " +   
		       "WHERE c.checkInId = :id")
	Optional<CheckIn> findByIdWithDetails(@Param("id") Long id);
		
}