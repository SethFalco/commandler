package com.elypia.commandler.jda.annotations.access;

import net.dv8tion.jda.core.entities.ChannelType;

import java.lang.annotation.*;

/**
 * For commands that are limited as to what channels they can
 * be performed in, specify the channel types this functionality
 * can be executed.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    ChannelType[] value();
}
