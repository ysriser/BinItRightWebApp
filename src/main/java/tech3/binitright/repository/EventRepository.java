package tech3.binitright.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech3.binitright.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Finds events where the end time is in the future (includes events happening now)
    // Sorted so the one ending soonest appears first
    List<Event> findByEndTimeAfterOrderByStartTimeAsc(LocalDateTime now);
}