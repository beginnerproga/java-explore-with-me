package ru.practicum.exceptions;

public class LikeAlreadySetException extends RuntimeException {
    public LikeAlreadySetException(String message) {
        super(message);
    }
}
