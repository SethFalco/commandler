package com.elypia.commandler.validation.constraints;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.api.Integration;
import com.elypia.commandler.event.ActionEvent;

import javax.validation.*;
import java.lang.annotation.*;

/** This constraint is {@link Commandler} specific. */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = Platform.Validator.class)
public @interface Platform {

    String message() default "{com.elypia.commandler.validation.constraints.Platform.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Class<? extends Integration>[] value();

    class Validator implements ConstraintValidator<Platform, ActionEvent<?, ?>> {

        private Class<? extends Integration>[] controllerTypes;

        @Override
        public void initialize(Platform constraintAnnotation) {
            this.controllerTypes = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(ActionEvent<?, ?> event, ConstraintValidatorContext context) {
            Integration controller = event.getIntegration();

            for (Class<? extends Integration> type : controllerTypes) {
                if (type == controller.getClass())
                    return true;
            }

            return false;
        }
    }
}
