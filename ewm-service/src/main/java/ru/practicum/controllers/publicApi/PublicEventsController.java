package ru.practicum.controllers.publicApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatisticClient;
import ru.practicum.info.EventInfoDto;
import ru.practicum.info.EventShortInfoDto;
import ru.practicum.models.EventSort;
import ru.practicum.servers.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@Validated
public class PublicEventsController {
    private final EventService eventService;
    private final StatisticClient statisticClient;

    @Autowired
    public PublicEventsController(EventService eventService, StatisticClient statisticClient) {
        this.eventService = eventService;
        this.statisticClient = statisticClient;
    }

    @GetMapping
    public List<EventShortInfoDto> getAllEvents(@RequestParam(name = "text", required = false) String text,
                                                @RequestParam(name = "categories", required = false) List<Long> categories,
                                                @RequestParam(name = "paid", required = false) Boolean paid,
                                                @RequestParam(name = "rangeStart", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(name = "rangeEnd", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(name = "sort", defaultValue = "EVENT_DATE") EventSort sort,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive int size,
                                                HttpServletRequest request) {
        log.info("Send request to static client to add EndpointHit");
        statisticClient.addEndpointHit(request);
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventInfoDto getEventById(@PathVariable("id") long eventId, HttpServletRequest request) {
        log.info("Send request to static client to add EndpointHit");
        statisticClient.addEndpointHit(request);
        return eventService.getEventById(eventId);
    }

    @GetMapping("/event_rating/top")
    public List<EventShortInfoDto> getEventsByEventRatingOfEvent(@RequestParam(required = false, defaultValue = "15") @Positive int count,
                                                                 @RequestParam(required = false, defaultValue = "true") boolean desc,
                                                                 HttpServletRequest request) {
        statisticClient.addEndpointHit(request);
        return eventService.getEventsByRating(count, desc, true);
    }

    @GetMapping("/user_rating/top")
    public List<EventShortInfoDto> getEventsByUserRatingOfEvent(@RequestParam(required = false, defaultValue = "15") @Positive int count,
                                                                @RequestParam(required = false, defaultValue = "true") boolean desc,
                                                                HttpServletRequest request) {
        statisticClient.addEndpointHit(request);
        return eventService.getEventsByRating(count, desc, false);
    }


}
