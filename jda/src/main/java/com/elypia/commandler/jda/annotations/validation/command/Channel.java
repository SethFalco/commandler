package com.elypia.commandler.jda.annotations.validation.command;

import net.dv8tion.jda.core.entities.ChannelType;

import java.lang.annotation.*;

/**
 * For commands that are limited as to what channels they can
 * be performed in, specify the channel types this functionality
 * can be executed.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Channel {

    /**
     * @return The types of channels it will be possible to perform
     * this commands in.
     */

    ChannelType[] value();
}
