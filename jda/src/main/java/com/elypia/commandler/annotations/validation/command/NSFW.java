package com.elypia.commandler.annotations.validation.command;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

/**
 * This is for denoting a commands which should only be possible if the channel is NSFW.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface NSFW {

}
