package ru.practicum.exceptions.exception404;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(String message) {
        super(message);
    }
}
