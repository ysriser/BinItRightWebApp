package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech3.binitright.model.Report;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE MONTH(r.generatedAt) = :month AND YEAR(r.generatedAt) = :year")
    List<Report> findByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
