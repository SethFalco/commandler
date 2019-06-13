package com.elypia.commandler;

public interface Handler {

    /**
     * Performs relevent tests to ensure this module
     * is still working as intended. Should any test fail we will.
     *
     * @return If the module should remain enabled.
     */
    default boolean test() {
        return true;
    }
}
