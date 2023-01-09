package ru.practicum.controllers.privateApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.servers.ParticipationRequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")

public class PrivateParticipationRequestsController {
    private final ParticipationRequestService participationRequestService;

    @Autowired
    public PrivateParticipationRequestsController(ParticipationRequestService participationRequestService) {
        this.participationRequestService = participationRequestService;
    }

    @PostMapping
    public ParticipationRequestDto addParticipationRequest(@PathVariable long userId, @RequestParam long eventId) {
        return participationRequestService.addParticipationRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable long userId) {
        return participationRequestService.getParticipationRequest(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable long userId, @PathVariable long requestId) {
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }
}
