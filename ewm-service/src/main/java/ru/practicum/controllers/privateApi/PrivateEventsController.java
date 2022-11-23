package ru.practicum.controllers.privateApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.info.EventInfoDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated

public class PrivateEventsController {
}
