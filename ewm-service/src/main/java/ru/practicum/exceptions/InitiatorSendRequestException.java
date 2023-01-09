package ru.practicum.exceptions;

public class InitiatorSendRequestException extends RuntimeException {
    public InitiatorSendRequestException(String message) {
        super(message);
    }
}
