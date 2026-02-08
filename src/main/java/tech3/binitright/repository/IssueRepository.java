package tech3.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech3.binitright.model.Issue;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByStatus(Issue.IssueStatus status);
    long countByStatus(Issue.IssueStatus status);
    long count();

    @Query("""
    SELECT i FROM Issue i
    JOIN FETCH i.raisedBy
    ORDER BY i.createdAt DESC""")
    List<Issue> findAllWithRaisedBy();

    @Query("""
            SELECT i FROM Issue i 
            JOIN FETCH i.raisedBy 
            ORDER BY i.createdAt DESC 
            LIMIT 5""")
    List<Issue> findTop5WithRaisedBy();

    @Query("""
        SELECT i
        FROM Issue i
        JOIN FETCH i.raisedBy
        WHERE i.issueId = :id
    """)
    Optional<Issue> findByIdWithRaisedBy(@Param("id") Long id);

}
