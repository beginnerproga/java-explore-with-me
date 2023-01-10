package ru.practicum.mappers;

import ru.practicum.dto.CompilationDto;
import ru.practicum.info.CompilationInfoDto;
import ru.practicum.models.Compilation;
import ru.practicum.models.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(CompilationDto compilationDto, List<Event> events) {
        return new Compilation(events, null, compilationDto.getTitle(), compilationDto.getPinned());
    }

    public static CompilationInfoDto toCompilationInfoDto(Compilation compilation) {
        return new CompilationInfoDto(compilation.getId(), compilation.getTitle(), compilation.getPinned(), compilation.getEvents().stream().map(EventMapper::toEventInfoDto)
                .collect(Collectors.toList()));
    }
}
