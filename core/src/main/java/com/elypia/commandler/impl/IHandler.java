package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.*;

import java.util.*;

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
public interface IHandler<C, E, M> extends Comparable<IHandler<C, E, M>> {

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
    default Object help(ICommandEvent<C, E, M> event) {
        StringBuilder builder = new StringBuilder();

        Module annotation = getModule().getModule();
        builder.append(annotation.name());

        StringJoiner commandAliasJoiner = new StringJoiner(", ");

        for (String alias : annotation.aliases())
            commandAliasJoiner.add(alias);

        builder.append(" (" + commandAliasJoiner.toString() + ")");
        builder.append("\n" + annotation.help());

        if (!isEnabled())
            builder.append("\n" + getConfiler().getMisuseListener().onModuleDisabled(event));

        builder.append("\n\n");

        Iterator<MetaCommand<C, E, M>> metaCommandIt = getModule().getPublicCommands().iterator();

        while (metaCommandIt.hasNext()) {
            MetaCommand<C, E, M> metaCommand = metaCommandIt.next();
            Command command = metaCommand.getCommand();
            builder.append(command.name());

            StringJoiner aliasJoiner = new StringJoiner(", ");

            for (String string : command.aliases())
                aliasJoiner.add(string);

            builder.append(" (" + aliasJoiner.toString() + ")");
            builder.append("\n" + command.help());

            List<MetaParam> metaParams = metaCommand.getInputParams();

            metaParams.forEach(metaParam -> {
                Param param = metaParam.getParamAnnotation();
                builder.append("\n" + param.name() + ": ");
                builder.append(getConfiler().getScript(event.getSource(), param.help(), Map.of()));
            });

            if (metaCommandIt.hasNext())
                builder.append("\n\n");
        }

        String helpUrl = getConfiler().getHelpUrl(event.getSource());

        if (helpUrl != null)
            builder.append(helpUrl);

        return builder.toString();
    }

    Commandler<C, E, M> getCommandler();

    IConfiler<C, E, M> getConfiler();

    MetaModule<C, E, M> getModule();

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
