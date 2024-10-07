package com.example.CRUD_Exception.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { DobValidation.class})
public @interface DobConstraint {
    String message() default "{jakarta.validation.constraints.Size.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int min() default 0;
}
