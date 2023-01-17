package ru.practicum.servers;

import ru.practicum.dto.EventDto;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.info.EventInfoDto;
import ru.practicum.info.EventShortInfoDto;
import ru.practicum.info.LikeInfoDto;
import ru.practicum.models.EventSort;
import ru.practicum.models.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventInfoDto updateEvent(long userId, EventDto eventDto);

    EventInfoDto updateEvent(EventDto eventDto, long eventId);

    EventInfoDto addEvent(long userId, EventDto eventDto);

    List<EventInfoDto> getEvents(long userId, int from, int size);

    EventInfoDto getEventById(long userId, long eventId);

    EventInfoDto getEventById(long eventId);

    EventInfoDto cancelEvent(long userId, long eventId);

    List<ParticipationRequestDto> getRequestsByEventId(long userId, long eventId);

    ParticipationRequestDto confirmRequestByRequestId(long userId, long eventId, long requestId);

    ParticipationRequestDto rejectRequestByRequestId(long userId, long eventId, long requestId);

    List<EventInfoDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventInfoDto publishEvent(long eventId);

    EventInfoDto rejectEvent(long eventId);

    List<EventShortInfoDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, int from, int size);

    LikeInfoDto addLikeToEvent(long userId, long eventId, boolean positive);

    List<EventShortInfoDto> getEventsByRating(int count, boolean desc, boolean eventRating);
}
