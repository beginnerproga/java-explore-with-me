package ru.practicum.mappers;

import ru.practicum.dto.EventDto;
import ru.practicum.info.EventInfoDto;
import ru.practicum.info.EventShortInfoDto;
import ru.practicum.models.Category;
import ru.practicum.models.Event;
import ru.practicum.models.EventState;
import ru.practicum.models.User;

import java.time.LocalDateTime;
import java.util.Optional;

public class EventMapper {
    public static Event toEvent(EventDto eventDto, User initiator, Category category) {
        return new Event(null, eventDto.getAnnotation(), category, 0L, LocalDateTime.now(),
                eventDto.getDescription(), eventDto.getEventDate(), initiator, eventDto.getLocation(), eventDto.getPaid(),
                eventDto.getParticipantLimit(), null, eventDto.getRequestModeration(), EventState.PENDING, eventDto.getTitle(), 0L, 0.0F);
    }

    public static EventInfoDto toEventInfoDto(Event event) {
        return new EventInfoDto(event.getAnnotation(), CategoryMapper.toCategoryDto(event.getCategory()), event.getConfirmedRequests(), event.getCreatedOn(), event.getDescription(),
                event.getEventDate(), event.getId(), UserMapper.toUserDto(event.getInitiator()), event.getLocation(), event.getPaid(), event.getParticipantLimit(), event.getPublishedOn(),
                event.getRequestModeration(), event.getState(), event.getTitle(), Optional.ofNullable(event.getViews()).orElse(0L), event.getRating());
    }

    public static EventShortInfoDto toEventShortInfoDto(Event event) {
        return new EventShortInfoDto(event.getId(), event.getTitle(), event.getAnnotation(), CategoryMapper.toCategoryDto(event.getCategory()), event.getConfirmedRequests(),
                event.getEventDate(), UserMapper.toUserShortDto(event.getInitiator()), event.getPaid(), Optional.ofNullable(event.getViews()).orElse(0L), event.getRating());
    }
}
