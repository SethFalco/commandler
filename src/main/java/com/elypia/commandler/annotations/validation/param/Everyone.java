package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.Validation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface Everyone {

}
