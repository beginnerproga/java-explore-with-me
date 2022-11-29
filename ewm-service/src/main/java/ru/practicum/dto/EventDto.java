package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.models.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private long id;
    @NotNull
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    @NotNull
    @NotBlank
    private String title;
}
