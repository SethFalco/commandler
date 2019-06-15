package com.elypia.commandler.constraints;

import com.elypia.commandler.*;

import javax.inject.Inject;
import javax.validation.*;
import java.lang.annotation.*;
import java.util.*;

/** This constraint is {@link Commandler} specific. */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = XContains.Validator.class)
public @interface XContains {

    String message() default "{com.elypia.commandler.constraints.XContains.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value();

    class Validator implements ConstraintValidator<XContains, String> {

        private CommandlerEvent<?> event;
        private String substring;

        @Inject
        public Validator(CommandlerEvent<?> event) {
            this.event = Objects.requireNonNull(event);
        }

        @Override
        public void initialize(XContains constraintAnnotation) {
            int index = constraintAnnotation.value();
            List<String> param = event.getInput().getParameters().get(index);

            if (param.size() > 1)
                throw new IllegalStateException("Can't perform XContains validation on a list.");

            substring = param.get(0);
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null)
                return false;

            return value.contains(substring);
        }
    }
}
