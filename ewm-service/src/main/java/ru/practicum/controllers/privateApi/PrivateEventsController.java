package ru.practicum.controllers.privateApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.EventDto;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.info.EventInfoDto;
import ru.practicum.info.LikeInfoDto;
import ru.practicum.servers.EventService;
import ru.practicum.util.Create;
import ru.practicum.util.Update;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class PrivateEventsController {
    private final EventService eventService;
    private final StatisticClient statisticClient;

    @Autowired
    public PrivateEventsController(EventService eventService, StatisticClient statisticClient) {
        this.eventService = eventService;
        this.statisticClient = statisticClient;
    }

    @PostMapping
    public EventInfoDto addEvent(@PathVariable long userId, @RequestBody @Validated(Create.class) @Valid EventDto eventDto) {
        return eventService.addEvent(userId, eventDto);
    }

    @PatchMapping
    public EventInfoDto updateEvent(@PathVariable long userId, @RequestBody @Validated({Update.class}) EventDto eventDto) {
        EventInfoDto eventInfoDto = eventService.updateEvent(userId, eventDto);
        eventInfoDto.setViews(statisticClient.getStats(eventInfoDto.getId()));
        return eventInfoDto;
    }

    @GetMapping
    public List<EventInfoDto> getEvents(@PathVariable long userId, @PositiveOrZero @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {
        List<EventInfoDto> eventInfoDtos = eventService.getEvents(userId, from, size);
        for (EventInfoDto eventInfoDto : eventInfoDtos)
            eventInfoDto.setViews(statisticClient.getStats(eventInfoDto.getId()));
        return eventInfoDtos;
    }

    @GetMapping("/{eventId}")
    public EventInfoDto getEventById(@PathVariable long userId, @PathVariable long eventId, HttpServletRequest httpServletRequest) {
        EventInfoDto eventInfoDto = eventService.getEventById(userId, eventId);
        eventInfoDto.setViews(statisticClient.getStats(eventInfoDto.getId()));
        return eventInfoDto;
    }

    @PatchMapping("/{eventId}")
    public EventInfoDto cancelEvent(@PathVariable long userId, @PathVariable long eventId) {
        EventInfoDto eventInfoDto = eventService.cancelEvent(userId, eventId);
        eventInfoDto.setViews(statisticClient.getStats(eventInfoDto.getId()));
        return eventInfoDto;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEventId(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequestByRequestId(@PathVariable long userId, @PathVariable long eventId, @PathVariable(name = "reqId") long requestId) {
        return eventService.confirmRequestByRequestId(userId, eventId, requestId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequestByRequestId(@PathVariable long userId, @PathVariable long eventId, @PathVariable(name = "reqId") long requestId) {
        return eventService.rejectRequestByRequestId(userId, eventId, requestId);
    }

    @PostMapping("/{eventId}/like")
    public LikeInfoDto addLikeToEvent(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.addLikeToEvent(userId, eventId, true);
    }

    @PostMapping("/{eventId}/dislike")
    public LikeInfoDto addDislikeToEvent(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.addLikeToEvent(userId, eventId, false);
    }

}

