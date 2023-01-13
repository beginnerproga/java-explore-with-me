package ru.practicum.mappers;

import ru.practicum.info.LikeInfoDto;
import ru.practicum.models.Like;

public class LikeMapper {
    public static LikeInfoDto toLikeInfoDto(Like like) {
        return new LikeInfoDto(like.getId(), like.getEvent(), like.getUser(), like.getPositive());
    }
}
