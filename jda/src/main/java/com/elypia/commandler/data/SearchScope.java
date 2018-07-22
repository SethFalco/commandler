package com.elypia.commandler.data;

public enum SearchScope {

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
