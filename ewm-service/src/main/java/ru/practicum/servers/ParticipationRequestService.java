package ru.practicum.servers;

import ru.practicum.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto addParticipationRequest(long userId, long eventId);

    List<ParticipationRequestDto> getParticipationRequest(long userId);

    ParticipationRequestDto cancelParticipationRequest(long userId, long requestId);
}
