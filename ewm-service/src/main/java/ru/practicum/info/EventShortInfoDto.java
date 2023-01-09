package ru.practicum.info;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.UserShortDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class EventShortInfoDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private long views;

}
