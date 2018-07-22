package com.elypia.commandler.annotations.validation.param;

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

    enum SearchScope {

        /**
         * Search everything that the bot can see.
         */
        GLOBAL,

        /**
         * Search mutual places that the performing user, and the bot
         * can both see.
         */
        MUTUAL,

        /**
         * Only search in the current group or channel.
         */
        LOCAL
    }

    /**
     * @return The scope to search for.
     */
    SearchScope value();
}

