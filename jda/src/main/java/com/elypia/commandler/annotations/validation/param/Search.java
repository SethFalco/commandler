package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.SearchScope;

import java.lang.annotation.*;

/**
 * Some objects in Discord can be searched but there are
 * various scopes to search from. For example searching a User
 * could be searched Locally, in the current chat only, Mutually
 * only in the current chat or in any mutual guilds, and Globally,
 * through all of Discord that the bot can see.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Search {

    /**
     * @return The scope to search for.
     */
    SearchScope value();
}

