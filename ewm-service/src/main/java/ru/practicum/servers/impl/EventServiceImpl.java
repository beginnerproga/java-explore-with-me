package ru.practicum.servers.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.EventDto;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.exceptions.InappropriateStateForAction;
import ru.practicum.exceptions.LikeAlreadySetException;
import ru.practicum.exceptions.UserNotAccessException;
import ru.practicum.exceptions.exception404.CategoryNotFoundException;
import ru.practicum.exceptions.exception404.EventNotFoundException;
import ru.practicum.exceptions.exception404.RequestNotFoundException;
import ru.practicum.exceptions.exception404.UserNotFoundException;
import ru.practicum.info.EventInfoDto;
import ru.practicum.info.EventShortInfoDto;
import ru.practicum.info.LikeInfoDto;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.LikeMapper;
import ru.practicum.mappers.ParticipationRequestMapper;
import ru.practicum.models.*;
import ru.practicum.repositories.*;
import ru.practicum.servers.EventService;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@Getter
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final VisitRepository visitRepository;
    private final LikeRepository likeRepository;
    private final StatisticClient statisticClient;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, ParticipationRequestRepository participationRequestRepository,
                            StatisticClient statisticClient, VisitRepository visitRepository, LikeRepository likeRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.participationRequestRepository = participationRequestRepository;
        this.statisticClient = statisticClient;
        this.visitRepository = visitRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    @Transactional
    public EventInfoDto updateEvent(long userId, EventDto eventDto) {
        log.info("Received request to update event from user with user's id={}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        Category category = null;
        if (eventDto.getCategoryId() != null && eventDto.getCategoryId() != 0)
            category = categoryRepository.findById(eventDto.getCategoryId()).orElseThrow(() -> {
                throw new CategoryNotFoundException("Category with id = " + eventDto.getCategoryId() + " not found");
            });
        Event event = EventMapper.toEvent(eventDto, user, category);
        Event result = eventRepository.findById(eventDto.getId()).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventDto.getId() + " not found");
        });
        if (result.getInitiator().getId() != userId)
            throw new UserNotAccessException("User with id = " + userId + " is not owner");
        if (event.getAnnotation() != null)
            result.setAnnotation(event.getAnnotation());
        if (event.getCategory() != null)
            result.setCategory(event.getCategory());
        if (event.getDescription() != null)
            result.setDescription(event.getDescription());
        if (event.getEventDate() != null)
            result.setEventDate(event.getEventDate());
        if (event.getPaid() != null)
            result.setPaid(event.getPaid());
        if (event.getParticipantLimit() != null)
            result.setParticipantLimit(event.getParticipantLimit());
        if (event.getTitle() != null)
            result.setTitle(event.getTitle());
        if (event.getLocation() != null)
            result.setLocation(event.getLocation());
        if (event.getRequestModeration() != null)
            result.setRequestModeration(event.getRequestModeration());
        eventRepository.save(result);
        event.setViews(Optional.ofNullable(statisticClient.getStats(List.of(eventDto.getId())).get(eventDto.getId())).orElse(0L));
        return EventMapper.toEventInfoDto(result);
    }

    @Override
    @Transactional
    public EventInfoDto updateEvent(EventDto eventDto, long eventId) {
        log.info("Received request to update event with event's id = {} from admin", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        Category category = categoryRepository.findById(eventDto.getCategoryId()).orElseThrow(() -> {
            throw new CategoryNotFoundException("Category with id = " + eventDto.getCategoryId() + " not found");
        });
        if (eventDto.getAnnotation() != null)
            event.setAnnotation(eventDto.getAnnotation());
        if (eventDto.getCategoryId() != null)
            event.setCategory(category);
        if (eventDto.getDescription() != null)
            event.setDescription(eventDto.getDescription());
        if (eventDto.getEventDate() != null)
            event.setEventDate(eventDto.getEventDate());
        if (eventDto.getPaid() != null)
            event.setPaid(eventDto.getPaid());
        if (eventDto.getParticipantLimit() != null)
            event.setParticipantLimit(eventDto.getParticipantLimit());
        if (eventDto.getTitle() != null)
            event.setTitle(eventDto.getTitle());
        if (eventDto.getLocation() != null)
            event.setLocation(eventDto.getLocation());
        if (eventDto.getRequestModeration() != null)
            event.setRequestModeration(eventDto.getRequestModeration());
        eventRepository.save(event);
        event.setViews(Optional.ofNullable(statisticClient.getStats(List.of(eventId)).get(eventId)).orElse(0L));
        return EventMapper.toEventInfoDto(event);
    }

    @Override
    @Transactional
    public EventInfoDto addEvent(long userId, EventDto eventDto) {
        log.info("Received request to save event from user with user's id={}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        Category category = categoryRepository.findById(eventDto.getCategoryId()).orElseThrow(() -> {
            throw new CategoryNotFoundException("Category with id = " + eventDto.getCategoryId() + " not found");
        });
        Event event = EventMapper.toEvent(eventDto, user, category);
        eventRepository.save(event);
        return EventMapper.toEventInfoDto(event);
    }

    @Override
    public List<EventInfoDto> getEvents(long userId, int from, int size) {
        log.info("Received request to get all events from user with user's id = {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        List<Event> events = eventRepository.findAllByInitiatorOrderById(user, pageable).getContent();
        if (events.size() != 0) {
            List<Long> ids = new ArrayList<>();
            HashMap<Long, Event> mapEvents = new HashMap<>();
            for (Event event : events) {
                ids.add(event.getId());
                mapEvents.put(event.getId(), event);
            }
            Map<Long, Long> answer = statisticClient.getStats(ids);
            for (Long id : answer.keySet())
                mapEvents.get(id).setViews(answer.get(id));
        }
        return events.stream().map(EventMapper::toEventInfoDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventInfoDto getEventById(long userId, long eventId) {
        log.info("Received request to get event with event's id = {} by user with user's id = {}", eventId, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");

        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        if (!event.getInitiator().equals(user))
            throw new UserNotAccessException("User with id = " + userId + " is not owner");
        event.setViews(Optional.ofNullable(statisticClient.getStats(List.of(eventId)).get(eventId)).orElse(0L));
        log.info("Add visit from user with id = {} to event with id ={}", userId, eventId);
        visitRepository.save(new Visit(null, event, user));
        return EventMapper.toEventInfoDto(event);
    }

    @Override
    public EventInfoDto getEventById(long eventId) {
        log.info("Received request to get event with event's id = {} from public controller", eventId);
        Event event = eventRepository.findByIdAndStateOrderById(eventId, EventState.PUBLISHED);
        if (event == null)
            throw new EventNotFoundException("Published event with id = " + eventId + " not found");
        event.setViews(Optional.ofNullable(statisticClient.getStats(List.of(eventId)).get(eventId)).orElse(0L));
        return EventMapper.toEventInfoDto(event);
    }

    @Override
    @Transactional
    public EventInfoDto cancelEvent(long userId, long eventId) {
        log.info("Received request to cancel event with event's id = {} by user with user's id = {}", eventId, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        if (!event.getInitiator().equals(user))
            throw new UserNotAccessException("User with id = " + userId + " is not owner");
        if (event.getState().equals(EventState.PENDING))
            event.setState(EventState.CANCELED);
        else
            throw new InappropriateStateForAction("Event's state is not PENDING");
        eventRepository.save(event);
        event.setViews(Optional.ofNullable(statisticClient.getStats(List.of(eventId)).get(eventId)).orElse(0L));
        return EventMapper.toEventInfoDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByEventId(long userId, long eventId) {
        log.info("Received request to get requests from event with event's id = {} by user with user's id = {}", eventId, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        if (!event.getInitiator().equals(user))
            throw new UserNotAccessException("User with id = " + userId + " is not owner");
        List<ParticipationRequest> participationRequests = participationRequestRepository.findAllByEvent(event);
        return participationRequests.stream().map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequestByRequestId(long userId, long eventId, long requestId) {
        log.info("Received request to confirm request with request's id = {} from event with event's id = {} by user with user's id = {}",
                requestId, eventId, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        if (!event.getInitiator().equals(user))
            throw new UserNotAccessException("User with id = " + userId + " is not owner");
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).orElseThrow(() -> {
            throw new RequestNotFoundException("Request with id = " + requestId + " not found");
        });
        if (!event.equals(participationRequest.getEvent()))
            throw new UserNotAccessException("Request is not for this event");
        long confirmedRequests = participationRequest.getEvent().getConfirmedRequests();
        long limit = participationRequest.getEvent().getParticipantLimit();
        if (limit != 0 && confirmedRequests == limit)
            throw new ValidationException("Application limit reached");
        if (confirmedRequests == limit - 1) {
            rejectAllPendingParticipationRequestsOfEvent(eventId);
        }
        participationRequest.setStatus(RequestStatus.CONFIRMED);
        event.setConfirmedRequests(++confirmedRequests);
        eventRepository.save(event);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequestByRequestId(long userId, long eventId, long requestId) {
        log.info("Received request to reject request with request's id = {} from event with event's id = {} by user with user's id = {}",
                requestId, eventId, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        if (!event.getInitiator().equals(user))
            throw new UserNotAccessException("User with id = " + userId + " is not owner");
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).orElseThrow(() -> {
            throw new RequestNotFoundException("Request with id = " + requestId + " not found");
        });
        if (!event.equals(participationRequest.getEvent()))
            throw new UserNotAccessException("Request is not for this event");
        participationRequest.setStatus(RequestStatus.REJECTED);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    public List<EventInfoDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        log.info("Received request to search events from admin");
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        List<Event> events = eventRepository.searchEvents(users, states, categories, rangeStart, rangeEnd, pageable);
        if (events.size() != 0) {
            List<Long> ids = new ArrayList<>();
            HashMap<Long, Event> mapEvents = new HashMap<>();
            for (Event event : events) {
                ids.add(event.getId());
                mapEvents.put(event.getId(), event);
            }
            Map<Long, Long> answer = statisticClient.getStats(ids);
            for (Long id : answer.keySet())
                mapEvents.get(id).setViews(answer.get(id));
        }
        return events.stream().map(EventMapper::toEventInfoDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventInfoDto publishEvent(long eventId) {
        log.info("Received request to publish event with event's id ={}", eventId);
        int seconds = 60 * 60;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        if (Duration.between(LocalDateTime.now(), event.getEventDate()).getSeconds() <= seconds)
            throw new ValidationException("the start date of the event must be no earlier than one hour from the date of publication.");
        if (!event.getState().equals(EventState.PENDING))
            throw new InappropriateStateForAction("Event's state is not pending");
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        eventRepository.save(event);
        event.setViews(Optional.ofNullable(statisticClient.getStats(List.of(eventId)).get(eventId)).orElse(0L));
        return EventMapper.toEventInfoDto(event);
    }

    @Override
    @Transactional
    public EventInfoDto rejectEvent(long eventId) {
        log.info("Received request to reject event with event's id ={}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        if (event.getState().equals(EventState.PUBLISHED))
            throw new InappropriateStateForAction("Event's state is published, error");
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        event.setViews(Optional.ofNullable(statisticClient.getStats(List.of(eventId)).get(eventId)).orElse(0L));
        return EventMapper.toEventInfoDto(event);

    }

    @Override
    public List<EventShortInfoDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, int from, int size) {
        log.info("Received request to search events from public controller");
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        if (rangeStart == null)
            rangeStart = LocalDateTime.now();
        List<Event> events = eventRepository.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable);
        if (events.size() != 0) {
            List<Long> ids = new ArrayList<>();
            HashMap<Long, Event> mapEvents = new HashMap<>();
            for (Event event : events) {
                ids.add(event.getId());
                mapEvents.put(event.getId(), event);
            }
            Map<Long, Long> answer = statisticClient.getStats(ids);
            for (Long id : answer.keySet())
                mapEvents.get(id).setViews(answer.get(id));
        }
        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    events = events.stream()
                            .sorted(Comparator.comparing(Event::getEventDate))
                            .collect(Collectors.toList());
                    break;
                case VIEWS:
                    events = events.stream().sorted(Comparator.comparingLong(Event::getViews).reversed())
                            .collect(Collectors.toList());
                    break;
            }
        } else
            events.stream().sorted(Comparator.comparingLong(Event::getId)).collect(Collectors.toList());
        return events.stream().map(EventMapper::toEventShortInfoDto).collect(Collectors.toList());
    }


    @Transactional
    public void rejectAllPendingParticipationRequestsOfEvent(long eventId) {
        List<ParticipationRequest> participationRequests = participationRequestRepository.findAllByStatusAndEvent_Id(RequestStatus.PENDING, eventId);
        participationRequests.forEach(a -> a.setStatus(RequestStatus.REJECTED));
        participationRequestRepository.saveAll(participationRequests);
    }

    @Override
    @Transactional
    public LikeInfoDto addLikeToEvent(long userId, long eventId, boolean positive) {
        log.info("Received request to add like to event with id = {} from user with id = {}", eventId, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event with id = " + eventId + " not found");
        });
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new InappropriateStateForAction("Event should be published");
        if (user.equals(event.getInitiator()))
            throw new UserNotAccessException("User with id = " + userId + " is initiator of event");
        if (visitRepository.findByEventAndUser(event, user) == null) {
            throw new UserNotAccessException("User with id = " + userId +
                    " doesn't visit the event with id = " + eventId);
        }
        if (likeRepository.findByUserAndEvent(user, event) != null)
            throw new LikeAlreadySetException("User with id = " + userId + "already set like");
        Like like = new Like(null, event, user, positive);
        likeRepository.save(like);
        long likesEvent = likeRepository.countAllByPositiveIsTrueAndEvent(event);
        long dislikesEvent = likeRepository.countAllByPositiveIsFalseAndEvent(event);
        float ratingEvent = calculateRating(likesEvent, dislikesEvent);
        long likesUser = likeRepository.countAllByPositiveIsTrueAndEvent_Initiator(event.getInitiator());
        long dislikesUser = likeRepository.countAllByPositiveIsFalseAndEvent_Initiator(event.getInitiator());
        float ratingUser = calculateRating(likesUser, dislikesUser);
        event.setRating(ratingEvent);
        event.getInitiator().setRating(ratingUser);
        eventRepository.save(event);
        userRepository.save(event.getInitiator());
        return LikeMapper.toLikeInfoDto(like);
    }

    @Override
    public List<EventShortInfoDto> getEventsByRating(int count, boolean desc, boolean eventRating) {
        log.info("Received request to get events by rating");
        List<Event> events;
        Pageable page = PageRequest.of(0, count);
        if (eventRating) {
            if (desc)
                events = eventRepository.findTopCountByOrderByRatingEventDesc(page);
            else
                events = eventRepository.findTopCountByOrderByRatingEventAsc(page);
        } else {
            if (desc)
                events = eventRepository.findTopCountByOrderByRatingUserDesc(page);
            else
                events = eventRepository.findTopCountByOrderByRatingUserAsc(page);
        }
        if (events.size() != 0) {
            List<Long> ids = new ArrayList<>();
            HashMap<Long, Event> mapEvents = new HashMap<>();
            for (Event event : events) {
                ids.add(event.getId());
                mapEvents.put(event.getId(), event);
            }
            Map<Long, Long> answer = statisticClient.getStats(ids);
            for (Long id : answer.keySet())
                mapEvents.get(id).setViews(answer.get(id));
        }
        return events.stream().map(EventMapper::toEventShortInfoDto).collect(Collectors.toList());
    }


    public Float calculateRating(Long likes, Long dislikes) {
        Float rating = ((likes.floatValue() - dislikes) / (likes.floatValue() + dislikes));
        rating = (new BigDecimal(rating)).setScale(2, RoundingMode.HALF_UP).floatValue();
        return rating;
    }
}
