package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.models.Event;
import ru.practicum.models.Like;
import ru.practicum.models.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndEvent(User user, Event event);

    Long countAllByEvent(Event event);

    Long countAllByPositiveIsTrueAndEvent(Event event);

    Long countAllByPositiveIsFalseAndEvent(Event event);

    Long countAllByPositiveIsTrueAndEvent_Initiator(User user);

    Long countAllByPositiveIsFalseAndEvent_Initiator(User user);
}
