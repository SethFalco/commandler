package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.Validation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(PARAMETER)
@Retention(RUNTIME)
@Validation
public @interface Everyone {

}
