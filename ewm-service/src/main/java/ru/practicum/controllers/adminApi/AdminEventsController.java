package ru.practicum.controllers.adminApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventDto;
import ru.practicum.info.EventInfoDto;
import ru.practicum.models.EventState;
import ru.practicum.servers.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventsController {
    private final EventService eventService;

    @Autowired
    public AdminEventsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventInfoDto> searchEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                           @RequestParam(name = "states", required = false) List<EventState> states,
                                           @RequestParam(name = "categories", required = false) List<Long> categories,
                                           @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                           @RequestParam(name = "from", defaultValue = "0", required = false) @PositiveOrZero int from,
                                           @RequestParam(name = "size", defaultValue = "10", required = false) @Positive int size) {

        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventInfoDto updateEvent(@PathVariable long eventId, @RequestBody @Validated EventDto eventDto) {
        return eventService.updateEvent(eventDto, eventId);
    }

    @PatchMapping("/{eventId}/publish")
    public EventInfoDto publishEvent(@PathVariable long eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventInfoDto rejectEvent(@PathVariable long eventId) {
        return eventService.rejectEvent(eventId);
    }
}
