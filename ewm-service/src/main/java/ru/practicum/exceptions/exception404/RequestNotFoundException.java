package ru.practicum.exceptions.exception404;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
