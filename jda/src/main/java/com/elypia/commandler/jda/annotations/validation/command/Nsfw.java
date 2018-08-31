package com.elypia.commandler.jda.annotations.validation.command;

import java.lang.annotation.*;

/**
 * This is for denoting a commands which should only be possible if the channel is NSFW.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Nsfw {

}
