package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * Each module can be assigned a default command.
 * The default commands is the default method we assume the user
 * has intended when they are attempting a command.
 * The default will be called if either no command is specified or
 * the "command" specified isn't a valid commands in the module. (Potentially a parameter.)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Default {

}
