package ru.practicum.servers.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.exceptions.InappropriateStateForAction;
import ru.practicum.exceptions.InitiatorSendRequestException;
import ru.practicum.exceptions.RepeatedRequestException;
import ru.practicum.exceptions.UserNotAccessException;
import ru.practicum.exceptions.exception404.EventNotFoundException;
import ru.practicum.exceptions.exception404.RequestNotFoundException;
import ru.practicum.exceptions.exception404.UserNotFoundException;
import ru.practicum.mappers.ParticipationRequestMapper;
import ru.practicum.models.*;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.ParticipationRequestRepository;
import ru.practicum.repositories.UserRepository;
import ru.practicum.servers.ParticipationRequestService;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@Getter
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public ParticipationRequestServiceImpl(ParticipationRequestRepository participationRequestRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.participationRequestRepository = participationRequestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(long userId, long eventId) {
        log.info("Received request to add request from user with user's id={}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        if (event.getInitiator().equals(user))
            throw new InitiatorSendRequestException("Initiator cannot send request to event");
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new InappropriateStateForAction("Event's state should be published");
        if (participationRequestRepository.findByEventAndRequester(event, user) != null)
            throw new RepeatedRequestException("Repeated request");
        if (event.getParticipantLimit() != 0 && Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests())) {
            throw new ValidationException("Reaching the application limit");
        }
        ParticipationRequest participationRequest = ParticipationRequestMapper.toParticipationRequest(event, user);
        if (!event.getRequestModeration())
            participationRequest.setStatus(RequestStatus.CONFIRMED);
        ParticipationRequest savedRequest = participationRequestRepository.save(participationRequest);
        return ParticipationRequestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequest(long userId) {
        log.info("Received request to get all requests from user with user's id={}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        List<ParticipationRequest> participationRequests = participationRequestRepository.findAllByRequester(user);
        return participationRequests.stream().map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(long userId, long requestId) {
        log.info("Received request to cancel participation request with id = {}", requestId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).orElseThrow(() -> {
            throw new RequestNotFoundException("Participation request with id = " + requestId + " not found");
        });
        if (!participationRequest.getRequester().equals(user))
            throw new UserNotAccessException("User cannot to cancel participation request");
        if (participationRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            Event event = eventRepository.findById(participationRequest.getEvent().getId()).orElseThrow(() -> {
                throw new EventNotFoundException("Event with this id not found");
            });
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        participationRequest.setStatus(RequestStatus.CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }
}
