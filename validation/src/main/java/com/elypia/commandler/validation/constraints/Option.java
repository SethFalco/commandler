package com.elypia.commandler.validation.constraints;

import javax.validation.*;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = Option.Validator.class)
public @interface Option {

    String message() default "{com.elypia.commandler.validation.constraints.Option.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /**
     * The available value for this console.
     */
    String[] value();

    class Validator implements ConstraintValidator<Option, String> {

        private String[] value;

        @Override
        public void initialize(Option constraintAnnotation) {
            value = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            for (String option : this.value) {
                if (option.equals(value))
                    return true;
            }

            return false;
        }
    }
}
