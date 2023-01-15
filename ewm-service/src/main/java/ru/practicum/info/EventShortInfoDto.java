package ru.practicum.info;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class EventShortInfoDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    @PositiveOrZero
    private long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private UserShortDto initiator;
    private boolean paid;
    @PositiveOrZero
    private Long views;
}
