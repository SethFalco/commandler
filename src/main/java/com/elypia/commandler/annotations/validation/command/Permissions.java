package com.elypia.commandler.annotations.validation.command;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;

/**
 * The permissions the bot needs in order to perform the command.
 * By default the user will also require these permission however this
 * can be overrideen by setting {@link #userRequiresPermission()} to false.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {

    /**
     * @return The permissions the bot and user would require in order
     * to perform the command.
     */

    Permission[] value();

    /**
     * @return If the user requires the permissions also to perform this command.
     */

    boolean userRequiresPermission() default true;
}
