package com.elypia.commandler.constraints;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.Controller;

import javax.validation.*;
import java.lang.annotation.*;

/** This constraint is {@link Commandler} specific. */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = Platform.Validator.class)
public @interface Platform {

    String message() default "{com.elypia.commandler.constraints.Platform.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Class<? extends Controller>[] value();

    class Validator implements ConstraintValidator<Platform, CommandlerEvent<?, ?>> {

        private Class<? extends Controller>[] controllerTypes;

        @Override
        public void initialize(Platform constraintAnnotation) {
            this.controllerTypes = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(CommandlerEvent<?, ?> event, ConstraintValidatorContext context) {
            Controller controller = event.getController();

            for (Class<? extends Controller> type : controllerTypes) {
                if (type == controller.getClass())
                    return true;
            }

            return false;
        }
    }
}
