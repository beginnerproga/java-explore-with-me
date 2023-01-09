package ru.practicum.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeNotEarlyNowValidator.class)
@Documented

public @interface TimeNotEarlyNow {

    String message() default "{TimeNotEarlyNow.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
