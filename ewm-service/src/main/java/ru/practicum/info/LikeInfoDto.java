package ru.practicum.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.models.Event;
import ru.practicum.models.User;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LikeInfoDto {
    private Long id;
    private Event event;
    private User user;
    private Boolean positive;
}
