package com.elypia.commandler.annotations.validation.command;

import com.elypia.commandler.annotations.validation.Validation;
import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;

/**
 * Check if the user has the {@link Permission#MANAGE_SERVER} permission. <br>
 * The bot doesn't have to have this permission.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface Elevated {

}
