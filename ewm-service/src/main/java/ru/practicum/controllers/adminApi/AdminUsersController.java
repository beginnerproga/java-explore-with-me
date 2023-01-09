package ru.practicum.controllers.adminApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.servers.UserService;
import ru.practicum.servers.impl.UserServiceImpl;
import ru.practicum.util.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class AdminUsersController {
    private final UserService userService;

    @Autowired
    public AdminUsersController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Validated(Create.class) UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public boolean deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return true;
    }

    @GetMapping
    public List<UserDto> getUsers(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
                                  @RequestParam(name = "ids", required = false) List<Long> ids) {
        return userService.getUsers(ids, from, size);
    }
}
