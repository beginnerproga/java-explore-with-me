package ru.practicum.models;

import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.UserShortDto;

import java.time.LocalDateTime;

public class Event {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Long views;

}
