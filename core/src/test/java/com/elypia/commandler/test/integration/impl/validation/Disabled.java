package com.elypia.commandler.test.integration.impl.validation;

import javax.validation.*;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Disabled.Validator.class)
public @interface Disabled {

    String message() default "{com.elypia.commandler.impl.validation.Disabled.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean value() default true;

    class Validator implements ConstraintValidator<Disabled, AbstractCommandlerEvent<String, String>> {

        private boolean isDisabled;

        @Override
        public void initialize(Disabled constraintAnnotation) {
            isDisabled = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(AbstractCommandlerEvent<String, String> value, ConstraintValidatorContext context) {
            return isDisabled;
        }
    }
}
