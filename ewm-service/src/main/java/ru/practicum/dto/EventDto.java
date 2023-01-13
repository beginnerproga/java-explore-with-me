package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.models.Location;
import ru.practicum.util.Create;
import ru.practicum.util.Update;
import ru.practicum.util.validator.TimeNotEarlyNow;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    @JsonProperty("eventId")
    @NotNull(groups = Update.class)
    private Long id;
    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    @Size(max = 1000)
    private String annotation;
    @NotNull(groups = {Create.class})
    @JsonProperty("category")
    private Long categoryId;
    @NotEmpty(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    @Size(max = 1000)
    private String description;
    @NotNull(groups = {Create.class})
    @TimeNotEarlyNow
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(groups = {Create.class})
    private Location location;
    private Boolean paid;
    @PositiveOrZero(groups = {Create.class, Update.class})
    private Long participantLimit;
    private Boolean requestModeration;
    @NotEmpty(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    @Size(max = 100)
    private String title;
}
