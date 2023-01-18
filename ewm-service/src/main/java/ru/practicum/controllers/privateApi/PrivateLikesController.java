package ru.practicum.controllers.privateApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.info.LikeInfoDto;
import ru.practicum.servers.EventService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}")
public class PrivateLikesController {
    private final EventService eventService;

    @Autowired

    public PrivateLikesController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/like")
    public LikeInfoDto addLikeToEvent(@PathVariable long userId, @PathVariable long eventId, HttpServletRequest request) {
        return eventService.addLikeToEvent(userId, eventId, true, request);
    }

    @PostMapping("/dislike")
    public LikeInfoDto addDislikeToEvent(@PathVariable long userId, @PathVariable long eventId, HttpServletRequest request) {
        return eventService.addLikeToEvent(userId, eventId, false, request);
    }
}
