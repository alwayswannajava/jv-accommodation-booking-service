package com.spring.booking.accommodationbookingservice.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
public @interface FieldMatch {
    String message() default "{FieldMatch.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String firstField();
    String secondField();
}
