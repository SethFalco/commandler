package com.elypia.commandler.annotations.validation.command;

import com.elypia.commandler.annotations.Validator;
import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;

/**
 * Check if the user has the {@link Permission#MANAGE_SERVER} permission. <br>
 * The bot doesn't have to have this permission.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validator("./resources/commands/elevated.svg")
public @interface Elevated {

}
