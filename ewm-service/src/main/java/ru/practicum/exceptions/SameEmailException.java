package ru.practicum.exceptions;

public class SameEmailException extends RuntimeException {
    public SameEmailException(String message) {
        super(message);
    }
}
