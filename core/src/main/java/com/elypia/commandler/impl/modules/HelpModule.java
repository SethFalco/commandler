package com.elypia.commandler.impl.modules;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.CommandlerEvent;
import com.elypia.commandler.metadata.Context;
import com.elypia.commandler.metadata.data.ModuleData;

import java.util.*;

/**
 * The default help module, this is an optional module
 * one can add to provide some basic help functionality.
 *
 * @param <S>
 * @param <M>
 */
@Module(name = "Help", aliases = "help")
public class HelpModule<S, M> extends Handler<S, M> {

    @Override
    @Default
    public Object help(CommandlerEvent<S, M> event) {
        return super.help(event);
    }

    @Overload("Help")
    public Object help(CommandlerEvent<S, M> event, @Param(name = "module", value = "module.h") String module) {
        Context context = commandler.getContext();

        ModuleData moduleData = context.getModule(module);

        if (moduleData == this.getModule())
            return help(event);

        if (moduleData != null)
            return commandler.getServiceProvider().get(moduleData.getModuleClass()).help(event);

        return language.get("value.module_not_found");
    }

    @Command(name = "Groups", aliases = {"group", "groups"}, help = "Display a list of all module groups.")
    public Object groups(CommandlerEvent<S, M> event) {
        StringBuilder builder = new StringBuilder(language.get(event.getSource(), "value.groups.title"));

        for (String group : commandler.getContext().getGroups(false).keySet())
            builder.append("\n- ").append(group);

        return builder.toString();
    }

    @Command(name = "value.modules.name", aliases = "modules", help = "value.modules.value")
    public Object modules(CommandlerEvent<S, M> event, @Param(name = "group", value = "The group to list modules for.") String query
    ) {
        var groups = commandler.getContext().getGroups(false);

        Optional<String> optGroupName = groups.keySet().stream().filter(
            name -> name.equalsIgnoreCase(query)
        ).findAny();

        if (!optGroupName.isPresent())
            return "That group doesn't exist.";

        S source = event.getSource();

        String groupName = optGroupName.get();
        Iterator<ModuleData> group = groups.get(groupName).iterator();

        StringBuilder builder = new StringBuilder(groupName);

        boolean disabled = false;

        while (group.hasNext()) {
            ModuleData module = group.next();

            builder.append("\n- ").append(module.getName());
            builder.append(" (").append(String.join(", ", module.getAliases())).append(")");

            if (commandler.getTestRunner().isFailing(this)) {
                builder.append(" *");
                disabled = true;
            }

            builder.append("\n").append(language.get(source, module.getHelp()));

            if (group.hasNext())
                builder.append("\n");
        }

        if (disabled)
            builder.append("\n\n").append(commandler.getMisuseHandler().onModuleDisabled(event));

        return builder.toString();
    }
}
