package com.elypia.commandler.annotations.validation.command;

import java.lang.annotation.*;

/**
 * This is for denoting a command which should only be possible if the channel is NSFW.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NSFW {

}
