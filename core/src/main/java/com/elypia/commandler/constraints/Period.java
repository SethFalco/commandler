package com.elypia.commandler.constraints;

import javax.validation.*;
import java.lang.annotation.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = Period.Validator.class)
public @interface Period {

    String message() default "{com.elypia.commandler.constraints.Period.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    long min() default 0;

    long max() default Integer.MAX_VALUE;

    /**
     * This is internally validated using {@link TimeUnit#SECONDS}
     * so setting this to {@link TimeUnit#NANOSECONDS} to specify
     * a time under a second shouldn't do anything.
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    class Validator implements ConstraintValidator<Period, Duration> {

        private static final TimeUnit SECONDS = TimeUnit.SECONDS;

        private long min;
        private long max;

        @Override
        public void initialize(Period constraintAnnotation) {
            TimeUnit unit = constraintAnnotation.unit();

            min = SECONDS.convert(constraintAnnotation.min(), unit);
            max = SECONDS.convert(constraintAnnotation.max(), unit);
        }

        @Override
        public boolean isValid(Duration value, ConstraintValidatorContext context) {
            long seconds = value.toSeconds();
            return (min <= seconds && seconds <= max);
        }
    }
}
