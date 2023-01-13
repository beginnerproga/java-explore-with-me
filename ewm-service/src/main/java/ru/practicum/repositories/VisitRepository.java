package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.models.Event;
import ru.practicum.models.User;
import ru.practicum.models.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    Visit findByEventAndUser(Event event, User user);
}
