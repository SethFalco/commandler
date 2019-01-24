package com.elypia.commandler;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.ICommandEvent;
import com.elypia.commandler.metadata.ModuleData;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Module(id = "Help", aliases = "help")
public class HelpModule<S, M> extends Handler<S, M> {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public HelpModule(Commandler<S, M> commandler) {
        super(commandler);
    }

    @Override
    @Default
    public Object help(ICommandEvent<S, M> event) {
        return super.help(event);
    }

    @Overload("Help")
    public Object help(
        ICommandEvent<S, M> event,
        @Param(id = "module", help = "module.h") String module
    ) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        ModulesContext context = commandler.getContext();

        ModuleData moduleData = context.getModule(module);

        if (moduleData == this.getModule())
            return help(event);

        if (moduleData != null)
            return moduleData.getInstance(commandler).help(event);

        return scripts.get("help.module_not_found");
    }

    @Command(id = "Groups", aliases = {"group", "groups"}, help = "Display a list of all module groups.")
    public Object groups(ICommandEvent<S, M> event) {
        StringBuilder builder = new StringBuilder(scripts.get(event.getSource(), "help.groups.title"));

        for (String group : commandler.getContext().getGroups(false).keySet())
            builder.append("\n- ").append(group);

        return builder.toString();
    }

    @Command(id = "help.modules.name", aliases = "modules", help = "help.modules.help")
    public Object modules(
        ICommandEvent<S, M> event,
        @Param(id = "group", help = "The group to list modules for.") String query
    ) {
        var groups = commandler.getContext().getGroups(false);

        Optional<String> optGroupName = groups.keySet().stream().filter(
            name -> name.equalsIgnoreCase(query)
        ).findAny();

        if (optGroupName.isEmpty())
            return "That group doesn't exist.";

        S source = event.getSource();

        String groupName = optGroupName.get();
        Iterator<ModuleData> group = groups.get(groupName).iterator();

        StringBuilder builder = new StringBuilder(groupName);

        boolean disabled = false;

        while (group.hasNext()) {
            ModuleData module = group.next();

            Module anno = module.getAnnotation();
            builder.append("\n- ").append(anno.id());
            builder.append(" (").append(String.join(", ", anno.aliases())).append(")");

            if (commandler.getTestRunner().isFailing(this)) {
                builder.append(" *");
                disabled = true;
            }

            builder.append("\n").append(scripts.get(source, anno.help()));

            if (group.hasNext())
                builder.append("\n");
        }

        if (disabled)
            builder.append("\n\n").append(commandler.misuseHandler.onModuleDisabled(event));

        return builder.toString();
    }
}
