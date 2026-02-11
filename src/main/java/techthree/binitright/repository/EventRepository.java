package techthree.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techthree.binitright.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Finds events where the end time is in the future (includes events happening now)
    // Sorted so the one ending soonest appears first
    List<Event> findByEndTimeAfterOrderByStartTimeAsc(LocalDateTime now);
}