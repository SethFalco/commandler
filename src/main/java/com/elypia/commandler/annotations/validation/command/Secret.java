package com.elypia.commandler.annotations.validation.command;

import com.elypia.commandler.annotations.Validation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validation("./resources/commands/secret.svg")
public @interface Secret {

}
