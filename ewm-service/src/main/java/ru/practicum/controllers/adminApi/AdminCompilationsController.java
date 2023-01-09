package ru.practicum.controllers.adminApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.info.CompilationInfoDto;
import ru.practicum.servers.CompilationService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationsController {
    private final CompilationService compilationService;

    @Autowired
    public AdminCompilationsController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationInfoDto addCompilation(@Validated @RequestBody CompilationDto compilationDto) {
        return compilationService.addCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public Boolean deleteCompilation(@PathVariable long compId) {
        return compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public Boolean deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        return compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public Boolean addEventToCompilation(@PathVariable long compId, @PathVariable long eventId) {
        return compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public Boolean unpinCompilation(@PathVariable long compId) {
        return compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public Boolean pinCompilation(@PathVariable long compId) {
        return compilationService.pinCompilation(compId);
    }
}
