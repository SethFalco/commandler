package com.elypia.commandler.jda.annotations;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {

    /**
     * @return The permissions the user would require to have in order to
     * perform this command.
     */

    Permission[] value();
}
