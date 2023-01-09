package ru.practicum.controllers.publicApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.info.CompilationInfoDto;
import ru.practicum.servers.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")

public class PublicCompilationsController {
    private final CompilationService compilationService;

    @Autowired
    public PublicCompilationsController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationInfoDto> getCompilations(@RequestParam(required = false, defaultValue = "true") boolean pinned,
                                                    @RequestParam(defaultValue = "0", required = false) @Positive int from,
                                                    @RequestParam(defaultValue = "10", required = false) @PositiveOrZero int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationInfoDto getCompilationById(@PathVariable long compId) {
        return compilationService.getCompilationById(compId);
    }
}
