package com.elypia.commandler.constraints;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.ServiceController;

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

    Class<? extends ServiceController<?, ?>>[] value();

    class Validator implements ConstraintValidator<Platform, CommandlerEvent<?>> {

        private Class<? extends ServiceController<?, ?>>[] platforms;

        @Override
        public void initialize(Platform constraintAnnotation) {
            this.platforms = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(CommandlerEvent<?> event, ConstraintValidatorContext context) {
            Class<?> serviceType = event.getService().getClass();

            for (Class<? extends ServiceController<?, ?>> service : platforms) {
                if (service == serviceType)
                    return true;
            }

            return false;
        }
    }
}
