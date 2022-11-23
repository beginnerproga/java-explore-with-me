package ru.practicum.servers;

import ru.practicum.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);
    void deleteUser(Long userId);
    List<UserDto> getUsers(List<Long> ids, int from, int size);
}
