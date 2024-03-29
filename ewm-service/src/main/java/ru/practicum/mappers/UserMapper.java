package ru.practicum.mappers;

import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.models.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRating());
    }

    public static User toUser(UserDto userDto) {
        return new User(null, userDto.getName(), userDto.getEmail(), 0.0F);
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

}
