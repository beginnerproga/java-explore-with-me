package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.models.Event;
import ru.practicum.models.ParticipationRequest;
import ru.practicum.models.RequestStatus;
import ru.practicum.models.User;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    ParticipationRequest findByEventAndRequester(Event event, User user);

    List<ParticipationRequest> findAllByRequester(User user);

    List<ParticipationRequest> findAllByEvent(Event event);

    List<ParticipationRequest> findAllByStatusAndEvent_Id(RequestStatus requestStatus, long eventId);
}
