package com.elypia.commandler.annotations.validation.command;

import com.elypia.commandler.annotations.Validator;
import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;

/**
 * The permissions the bot needs in order to perform the commands.
 * By default the user will also require these permission however this
 * can be overrideen by setting {@link #userRequiresPermission()} to false.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validator("./resources/commands/permissions.svg")
public @interface Permissions {

    /**
     * @return The permissions the bot and user would require in order
     * to perform the commands.
     */

    Permission[] value();

    /**
     * @return If the user requires the permissions also to perform this commands.
     */

    boolean userRequiresPermission() default true;
}
