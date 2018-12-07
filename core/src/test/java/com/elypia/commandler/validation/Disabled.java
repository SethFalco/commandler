package com.elypia.commandler.validation;

import com.elypia.commandler.impl.TestEvent;

import javax.validation.*;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Disabled.Validator.class)
public @interface Disabled {

    String message() default "{com.elypia.commandler.validation.Disabled.message}";

    boolean value() default true;

    class Validator implements ConstraintValidator<Disabled, TestEvent> {

        private boolean isDisabled;

        @Override
        public void initialize(Disabled constraintAnnotation) {
            isDisabled = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(TestEvent value, ConstraintValidatorContext context) {
            return isDisabled;
        }
    }
}
