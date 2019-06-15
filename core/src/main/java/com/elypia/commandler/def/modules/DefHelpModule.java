package com.elypia.commandler.def.modules;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.core.Context;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.*;
import com.elypia.commandler.testing.TestRunner;

import javax.inject.*;
import java.util.*;

/**
 * The default help module, this is an optional module
 * one can add to provide some basic help functionality.
 */
@Singleton
@Module(name = "Help", aliases = {"help", "h"})
public class DefHelpModule implements Handler {

    private Context context;
    private LanguageManager language;
    private TestRunner testRunner;

    @Inject
    public DefHelpModule(Context context, LanguageManager language, TestRunner testRunner) {
        this.context = context;
        this.language = language;
        this.testRunner = testRunner;
    }

    @Command(name = "Groups", aliases = {"groups", "group"}, help = "List all groups.")
    public String listGroups(CommandlerEvent<?> event) {
        StringJoiner joiner = new StringJoiner("\n", "Groups", "");

        for (String group : context.getGroups(false).keySet())
            joiner.add("* " + group);

        return joiner.toString();
    }

    @Command(name = "Modules", aliases = {"modules", "module", "mod"}, help = "List all modules of the specified group.")
    public String listModules(@Param(name = "group", help = "The group to list modules for.") String query) {
        var groups = context.getGroups(false);

        Optional<String> optGroupName = groups.keySet().stream().filter(
            name -> name.equalsIgnoreCase(query)
        ).findAny();

        if (optGroupName.isEmpty())
            return "No such group exists.";

        String groupName = optGroupName.get();
        List<MetaModule> modules = groups.get(groupName);
        boolean disabled = false;

        StringJoiner joiner = new StringJoiner("\n", groupName, "");

        for (MetaModule module : modules) {
            StringBuilder builder = new StringBuilder("* ")
                .append(module.toString());

            if (testRunner.isFailing(this)) {
                builder.append(" *");
                disabled = true;
            }

            builder
                .append("\n")
                .append(module.getHelp());

            joiner.add(builder.toString());
        }

        if (disabled)
            joiner.add("* Disabled due to live issues.");

        return joiner.toString();
    }

    /**
     * The default help command for a {@link Handler},
     * this should use the {@link MetaModule} around
     * this {@link Handler} to display helpful information
     * to the user.
     *
     * @param event The {@link CommandlerEvent event} produced by Commandler.
     * @return The message to send to the end user.
     */
    @Command(name = "Commands", aliases = {"commands", "command"}, help = "List all commands of the specified module.")
    public String listCommands(CommandlerEvent event, MetaModule module) {
        StringBuilder builder = new StringBuilder(module.getName());

        builder
            .append(" (")
            .append(String.join(", ", context.getAliases()))
            .append(")\n")
            .append(module.getHelp());

        if (testRunner.isFailing(this))
            builder.append("\n").append("This module is failing it's tests");

        builder.append("\n\n");

        Iterator<MetaCommand> metaCommandIt = module.getPublicCommands().iterator();

        while (metaCommandIt.hasNext()) {
            MetaCommand command = metaCommandIt.next();
            builder.append(language.get(event, command.getName()));

            builder
                .append(" (")
                .append(String.join(", ", command.getAliases()))
                .append(")\n")
                .append(language.get(event, command.getHelp()));

            List<MetaParam> params = command.getParams();

            params.forEach((param) -> {
                builder.append("\n" + language.get(event, param.getName()) + ": ");
                builder.append(language.get(event, param.getHelp()));
            });

            if (metaCommandIt.hasNext())
                builder.append("\n\n");
        }

        return builder.toString();
    }
}
