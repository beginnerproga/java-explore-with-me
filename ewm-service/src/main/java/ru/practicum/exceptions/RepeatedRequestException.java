package ru.practicum.exceptions;

public class RepeatedRequestException extends RuntimeException {
    public RepeatedRequestException(String message) {
        super(message);
    }
}
