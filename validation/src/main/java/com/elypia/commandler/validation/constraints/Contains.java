package com.elypia.commandler.validation.constraints;

import javax.validation.*;
import java.lang.annotation.*;
import java.util.Objects;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = Contains.Validator.class)
public @interface Contains {

    String message() default "{com.elypia.commandler.validation.constraints.Contains.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String value();

    class Validator implements ConstraintValidator<Contains, String> {

        private String value;

        @Override
        public void initialize(Contains constraintAnnotation) {
            this.value = Objects.requireNonNull(constraintAnnotation.value());
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null)
                return false;

            return value.contains(this.value);
        }
    }
}
