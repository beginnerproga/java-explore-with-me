package ru.practicum.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompilationInfoDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventInfoDto> events;
}
