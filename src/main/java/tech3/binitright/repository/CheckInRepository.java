package tech3.binitright.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech3.binitright.model.CheckIn;
import tech3.binitright.model.User;
import tech3.binitright.response.RecycleHistoryResponse;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
	//List<CheckIn> findByStatus(Status status);
	
	@Query("SELECT ci " +
		   "FROM CheckIn ci " +
		   "JOIN FETCH ci.wasteCategories wc " +
		   "JOIN FETCH ci.user u " +
		   "WHERE ci.status = :status")
	List<CheckIn> findByStatusWithDetails(@Param("status") CheckIn.Status status);

	@Query("SELECT c FROM CheckIn c " +
		   "JOIN FETCH c.user " +
		   "JOIN FETCH c.wasteCategories " +
		   "JOIN FETCH c.dropOffLocation " +
		   "WHERE c.checkInId = :id")
	Optional<CheckIn> findByIdWithDetails(@Param("id") Long id);

	@Query("SELECT c " +
		   "FROM CheckIn c " +
		   "JOIN FETCH c.user " +
		   "JOIN FETCH c.wasteCategories " +
		   "JOIN FETCH c.dropOffLocation")
	List<CheckIn> findAllWithDetails();

    @Query("""
    SELECT new tech3.binitright.response.RecycleHistoryResponse(
        wc.name,
        wc.iconUrl,
        ci.checkInTime,
        ci.quantity
    )
    FROM CheckIn ci
    JOIN ci.wasteCategories wc
    WHERE ci.user.id = :userId
    ORDER BY ci.checkInTime DESC
""")
    List<RecycleHistoryResponse> findRecycleHistoryByUserId(@Param ("userId")Long userId);

	long countByUser(User user);

    @Query("SELECT COALESCE(SUM(c.quantity),0) FROM CheckIn c WHERE c.user.id = :userId AND c.status = 'APPROVED'")
    Integer getTotalRecycledByUser(@Param("userId") Long userId);

    @Query("""
    SELECT ci
    FROM CheckIn ci
    JOIN FETCH ci.wasteCategories wc
    WHERE ci.user.id = :userId AND ci.status = tech3.binitright.model.CheckIn.Status.APPROVED
""")
    List<CheckIn> findApprovedByUserIdWithCategory(@Param("userId") Long userId);

    @Query("""
    SELECT COUNT(ci)
    FROM CheckIn ci
    WHERE ci.user.id = :userId AND ci.status = tech3.binitright.model.CheckIn.Status.PROCESSING
""")
    Long countPendingByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT ci
    FROM CheckIn ci
    JOIN FETCH ci.wasteCategories wc
    JOIN FETCH ci.user u
    JOIN FETCH ci.dropOffLocation d
    WHERE ci.status = tech3.binitright.model.CheckIn.Status.PROCESSING
    ORDER BY ci.checkInTime DESC
""")
    List<CheckIn> findPendingWithDetails();
}