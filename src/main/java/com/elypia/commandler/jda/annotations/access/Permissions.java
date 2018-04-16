package com.elypia.commandler.jda.annotations.access;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {

    /**
     * @return The permissions the user would require to have in order to
     * perform this command.
     */

    Permission[] value();
}
