package ru.practicum.servers.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.UserDto;
import ru.practicum.exceptions.SameEmailException;
import ru.practicum.exceptions.exception404.UserNotFoundException;
import ru.practicum.mappers.UserMapper;
import ru.practicum.models.User;
import ru.practicum.repositories.UserRepository;
import ru.practicum.servers.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@Getter
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("Received request to add user");
        User user = UserMapper.toUser(userDto);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new SameEmailException("user with this email already created");
        }
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info("Received request to delete a user by userId={}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User with id = " + userId + " not found");
        });
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        List<UserDto> result;
        if (ids != null && ids.size() != 0) {
            log.info("Received request to get all users by userIds={}", ids);
            result = userRepository.findAllByIdInOrderById(ids).stream()
                    .map(UserMapper::toUserDto).collect(Collectors.toList());

        } else {
            log.info("Received request to get all users", ids);
            int page = from / size;
            Pageable pageable = PageRequest.of(page, size);
            result = userRepository.findAll(pageable).get()
                    .map(UserMapper::toUserDto).collect(Collectors.toList());
        }
        return result;
    }

}
