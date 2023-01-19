package ru.practicum.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.models.Event;
import ru.practicum.models.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LikeInfoDto {
    @NotNull
    @Positive
    private Long id;
    @NotNull
    private Event event;
    @NotNull
    private User user;
    @NotNull
    private Boolean positive;
}