package techthree.binitright.repository;



import org.springframework.data.repository.query.Param;
import techthree.binitright.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
    @Query("""
    SELECT cat.name,
           SUM(c.quantity * cat.avgWeight)
    FROM CheckIn c
    JOIN c.wasteCategories cat
    WHERE FUNCTION('MONTH', c.checkInTime) = :month
      AND FUNCTION('YEAR', c.checkInTime) = :year
    GROUP BY cat.name
    ORDER BY SUM(c.quantity * cat.avgWeight) DESC
""")
    List<Object[]> getWeightDistributionByMonth(int month, int year);



}