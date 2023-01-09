package ru.practicum.servers.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CompilationDto;
import ru.practicum.exceptions.exception404.CompilationNotFoundException;
import ru.practicum.exceptions.exception404.EventNotFoundException;
import ru.practicum.info.CompilationInfoDto;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.models.Compilation;
import ru.practicum.models.Event;
import ru.practicum.repositories.CompilationRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.servers.CompilationService;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@Getter
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public CompilationInfoDto addCompilation(CompilationDto compilationDto) {
        log.info("Received request to add compilation");
        try {
            List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
            Compilation compilation = CompilationMapper.toCompilation(compilationDto, events);
            compilationRepository.save(compilation);
            return CompilationMapper.toCompilationInfoDto(compilation);
        } catch (RuntimeException e) {
            throw new ValidationException("Id or id's in compilation incorrect" + compilationDto.getEvents());
        }

    }

    @Override
    @Transactional
    public Boolean deleteCompilation(long compId) {
        log.info("Received request to delete compilation with compilation's id = {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation with id = " + compId + " not found"));
        compilationRepository.delete(compilation);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteEventFromCompilation(long compId, long eventId) {
        log.info("Received request to delete event with event's id = {} from compilation with compilation's id = {}", eventId, compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation with id = " + compId + " not found"));
        Event event = eventRepository.findById(eventId).
                orElseThrow(() -> {
                    throw new EventNotFoundException("Event with id = " + eventId + " not found");
                });
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
        return true;
    }

    @Override
    @Transactional
    public Boolean addEventToCompilation(long compId, long eventId) {
        log.info("Received request to add event with event's id = {} to compilation with compilation's id = {}", eventId, compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation with id = " + compId + " not found"));
        Event event = eventRepository.findById(eventId).
                orElseThrow(() -> {
                    throw new EventNotFoundException("Event with id = " + eventId + " not found");
                });
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
        return true;
    }

    @Override
    @Transactional
    public Boolean unpinCompilation(long compId) {
        log.info("Received request to unpin compilation with compilation's id = {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation with id = " + compId + " not found"));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        return true;
    }

    @Override
    @Transactional
    public Boolean pinCompilation(long compId) {
        log.info("Received request to pin compilation with compilation's id = {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation with id = " + compId + " not found"));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        return true;
    }

    @Override
    public List<CompilationInfoDto> getCompilations(boolean pinned, int from, int size) {
        log.info("Received request to get all compilations");
        int page = from / size;
        List<Compilation> compilations = compilationRepository.findAllByPinnedOrderByIdAsc(pinned, PageRequest.of(page, size)).getContent();
        return compilations.stream().map(CompilationMapper::toCompilationInfoDto).collect(Collectors.toList());
    }

    @Override
    public CompilationInfoDto getCompilationById(long compId) {
        log.info("Received request to get compilation with compilation's id = {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> {
            throw new CompilationNotFoundException("Compilation with id = " + compId + " not found");
        });
        return CompilationMapper.toCompilationInfoDto(compilation);
    }
}
