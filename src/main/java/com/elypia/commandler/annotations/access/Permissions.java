package com.elypia.commandler.annotations.access;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;

/**
 * The permissions the user and bot would both require in order to
 * perform the command. This is synced up so if the bot required a permission
 * to delete messages in the channel, the user would require the same permissions
 * in order to trigger the command on the bot to perform the action.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {

    /**
     * @return The permissions the bot and user would require in order
     * to perform the command.
     */

    Permission[] value();
}
