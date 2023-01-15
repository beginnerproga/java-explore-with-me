package ru.practicum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Event;
import ru.practicum.models.EventState;
import ru.practicum.models.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorOrderById(User user, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN :users)" +
            "AND ((:states) IS NULL OR e.state IN :states) " +
            "AND ((:catIds) IS NULL OR e.category.id IN :catIds) " +
            "AND ((CAST(:rangeStart AS date) is null  or CAST(:rangeEnd AS date) is null) OR ( e.eventDate between :rangeStart AND :rangeEnd))")
    List<Event> searchEvents(List<Long> users, List<EventState> states,
                             List<Long> catIds, LocalDateTime rangeStart,
                             LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e from Event as e " +
            "WHERE (e.state = 'PUBLISHED')" +
            "AND ((:text) IS NULL OR (lower(e.annotation) LIKE lower(CONCAT('%',:text,'%')) OR LOWER(e.description) LIKE lower(CONCAT('%',:text,'%'))))" +
            "AND ((:categories) IS NULL OR e.category.id in :categories)" +
            "AND((:paid) IS NULL OR e.paid = :paid)" +
            "AND ((:onlyAvailable) IS NULL OR ((:onlyAvailable = true) AND (e.confirmedRequests>=e.participantLimit or e.participantLimit = 0)) OR ((:onlyAvailable = false)))" +
            "AND ((CAST(:rangeStart as date) IS NULL) OR CAST(e.eventDate as date) > CAST(:rangeStart as date))" +
            "AND ((CAST(:rangeEnd as date) IS NULL) OR CAST(e.eventDate as date) < CAST(:rangeEnd as date)) ORDER BY e.id")
    List<Event> searchEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);

    Event findByIdAndStateOrderById(long eventId, EventState eventState);
}

