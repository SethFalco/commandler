package com.elypia.commandler.interfaces;

/**
 * When using param objects, it's not possible for Commandler
 * to determine the order the parameters should be in on it's own
 * so this interface should be implemented in order to write a
 * method that can return the parameters in the order required.
 */
public interface CommandParams {

    /**
     * @return An array of field names this parameter
     * object represents in the order they are expected by
     * the end-user.
     */
    String[] getParams();
}
