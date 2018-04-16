package com.elypia.commandler.jda.annotations.access;

import net.dv8tion.jda.core.entities.ChannelType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
