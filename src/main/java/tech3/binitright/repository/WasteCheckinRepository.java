package tech3.binitright.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tech3.binitright.model.CheckIn;

public interface WasteCheckinRepository extends JpaRepository<CheckIn, Long> {

    @Query("SELECT SUM(c.quantity * cat.avgWeight) FROM CheckIn c JOIN c.wasteCategories cat " +
            "WHERE FUNCTION('MONTH', c.checkInTime) = :month " +
            "AND FUNCTION('YEAR', c.checkInTime) = :year")
    Object calculateWeightByMonth(int month, int year);

    @Query("SELECT SUM(c.quantity * cat.avgWeight * cat.emissionFactor) FROM CheckIn c JOIN c.wasteCategories cat " +
            "WHERE FUNCTION('MONTH', c.checkInTime) = :month " +
            "AND FUNCTION('YEAR', c.checkInTime) = :year")
    Object calculateCO2ByMonth(int month, int year);

    @Query("SELECT COUNT(DISTINCT c.user.id) FROM CheckIn c " +
            "WHERE FUNCTION('MONTH', c.checkInTime) = :month " +
            "AND FUNCTION('YEAR', c.checkInTime) = :year")
    Long countParticipantsByMonth(int month, int year);
}