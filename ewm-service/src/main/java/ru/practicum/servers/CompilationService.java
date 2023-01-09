package ru.practicum.servers;

import ru.practicum.dto.CompilationDto;
import ru.practicum.info.CompilationInfoDto;

import java.util.List;

public interface CompilationService {
    CompilationInfoDto addCompilation(CompilationDto compilationDto);

    Boolean deleteCompilation(long compId);

    Boolean deleteEventFromCompilation(long compId, long eventId);

    Boolean addEventToCompilation(long compId, long eventId);

    Boolean unpinCompilation(long compId);

    Boolean pinCompilation(long compId);

    List<CompilationInfoDto> getCompilations(boolean pinned, int from, int size);

    CompilationInfoDto getCompilationById(long compId);
}
