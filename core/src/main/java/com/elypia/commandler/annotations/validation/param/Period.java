package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Validation("./resources/params/period.svg")
public @interface Period {

    long min() default 0;
    long max() default Long.MAX_VALUE;

    TimeUnit unit() default TimeUnit.SECONDS;
}
