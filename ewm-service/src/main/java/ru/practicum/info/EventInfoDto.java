package ru.practicum.info;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.UserDto;
import ru.practicum.models.EventState;
import ru.practicum.models.Location;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class EventInfoDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserDto initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
}
