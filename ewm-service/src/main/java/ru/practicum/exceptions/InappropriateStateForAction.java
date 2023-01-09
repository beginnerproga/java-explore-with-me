package ru.practicum.exceptions;

public class InappropriateStateForAction extends RuntimeException {
    public InappropriateStateForAction(String message) {
        super(message);
    }
}
