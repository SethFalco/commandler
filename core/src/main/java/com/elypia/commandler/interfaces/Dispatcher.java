package com.elypia.commandler.interfaces;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.exceptions.*;

/**
 * The {@link Dispatcher} has the role of processing an event
 * from your respective platform into something that can be interperted
 * by Commandler, this should be used to verify if it's a command as well
 * adapt it into an input and event object to be used internally.
 */
public interface Dispatcher {

    /**
     * @param event The source event that called this command.
     * @param content The content of the command.
     * @return If this is a valid command or not.
     */
    boolean isValid(Object event, String content);

    /**
     * Receieve and handles the event.
     *
     * @param controller The name of the service that recieved this.
     * @param content The content of the message.
     * @return The response to this command, or null
     * if this wasn't a command at all.
     */
    Object dispatch(Controller controller, Object event, String content);

    /**
     * Break the command down into it's individual components.
     *
     * @param controller The name of the service that recieved this.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    CommandlerEvent parse(Controller controller, Object source, String content) throws OnlyPrefixException, NoDefaultCommandException, ModuleNotFoundException, ParamCountMismatchException;
}
