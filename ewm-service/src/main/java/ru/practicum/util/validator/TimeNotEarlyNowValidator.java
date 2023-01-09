package ru.practicum.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalDateTime;

public class TimeNotEarlyNowValidator implements ConstraintValidator<TimeNotEarlyNow, LocalDateTime> {
    private static final long SECONDS = 2 * 60 * 60;

    @Override
    public boolean isValid(LocalDateTime time, ConstraintValidatorContext constraintValidatorContext) {
        return time.isAfter(LocalDateTime.now()) && Duration.between(LocalDateTime.now(), time).getSeconds() >= SECONDS;
    }
}
