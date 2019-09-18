package org.elypia.commandler.api;

public interface Controller {

    /**
     * Performs relevent tests to ensure this module
     * is still working as intended. Commandler
     * will disable the module until the test passes again
     * if it fails.
     *
     * @return If the module should remain enabled.
     */
    default boolean test() {
        return true;
    }
}
