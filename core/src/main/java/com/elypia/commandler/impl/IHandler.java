package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.Ignore;
import com.elypia.commandler.metadata.MetaModule;

/**
 * The {@link IHandler} is the interface which defines methods
 * a {@link IHandler command handler} must have.
 * This should be implemented for the platform you're intergrating in
 * order to make a more specific implementation and then
 * {@link Module modules} can be made using classes inheriting that.
 *
 * @param <C> The client we're integrating with.
 * @param <E> The type of event we're processing.
 * @param <M> The type of message we're receieving and sending.
 */

public interface IHandler<C, E, M> {

    /**
     * This initializes the {@link IHandler}. This is used
     * to perform common initialization for all command handlers
     * that get registered to our {@link Commandler}. <br>
     * We just perform this here to save us from having to pass
     * the Commandler manually to every {@link IHandler command handler}
     * and call the super contructor.
     *
     * @param commandler Our parent Commandler class.
     * @return If the command initialised succesfully or if something went wrong.
     */

    boolean init(Commandler<C, E, M> commandler);

    /**
     * This is a test which should be called on instantiating the {@link IHandler} by
     * {@link Commandler} as well as periodically throughout the application lifetime.
     * This will test if the {@link IHandler} is still working as intended or if
     * something is failing. Should tests fail the module will be disabled.
     *
     * @return If the tests passed or failed.
     */

    boolean test();

    /**
     * The default help command for a {@link IHandler},
     * this should use the {@link MetaModule} around
     * this {@link IHandler} to display helpful information
     * to the user.
     *
     * @param event The {@link CommandEvent event} produced by Commandler.
     * @return The message to send to the end user.
     */

    @Ignore
    @Command(name = "Help", aliases = "help")
    Object help(CommandEvent<C, E, M> event);

    MetaModule<C, E, M> getModule();
}
