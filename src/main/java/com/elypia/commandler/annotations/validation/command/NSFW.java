package com.elypia.commandler.annotations.validation.command;

import com.elypia.commandler.annotations.Validator;

import java.lang.annotation.*;

/**
 * This is for denoting a commands which should only be possible if the channel is NSFW.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validator("./resources/commands/nsfw.svg")
public @interface NSFW {

}
