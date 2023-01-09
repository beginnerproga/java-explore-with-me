package ru.practicum.mappers;

import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.models.Event;
import ru.practicum.models.ParticipationRequest;
import ru.practicum.models.RequestStatus;
import ru.practicum.models.User;

import java.time.LocalDateTime;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(participationRequest.getId(), participationRequest.getEvent().getId(), participationRequest.getRequester().getId(),
                participationRequest.getStatus(), participationRequest.getCreated());
    }

    public static ParticipationRequest toParticipationRequest(Event event, User user) {
        return new ParticipationRequest(null, user, event, LocalDateTime.now(), RequestStatus.PENDING);
    }
}
