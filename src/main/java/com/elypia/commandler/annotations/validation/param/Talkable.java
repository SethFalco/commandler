package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Validation("./resources/params/talkable.svg")
public @interface Talkable {

}
