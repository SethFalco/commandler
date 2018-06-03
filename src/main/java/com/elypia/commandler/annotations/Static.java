package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * A static command is a command which belongs to a module but
 * can also be used without specifying the module what so ever.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Static {

}
