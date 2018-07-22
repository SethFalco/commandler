package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * Each module can be assigned a default commands.
 * The default commands is the default method we assume the user
 * has intended when they are attempting a commands.
 * The default will be called if either no commands is specified or
 * the "commands" specified isn't a valid commands in the module. (Potentially a parameter.)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Default {
    
}
