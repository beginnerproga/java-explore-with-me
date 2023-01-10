package ru.practicum.exceptions;

public class UserNotAccessException extends RuntimeException {
    public UserNotAccessException(String message) {
        super(message);
    }
}
