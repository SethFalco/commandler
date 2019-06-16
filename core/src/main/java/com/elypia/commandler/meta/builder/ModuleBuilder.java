package com.elypia.commandler.meta.builder;

import com.elypia.commandler.exceptions.init.ConflictingModuleException;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.meta.ContextLoader;
import com.elypia.commandler.meta.data.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.*;

public class ModuleBuilder implements Iterable<CommandBuilder> {

    private static final Logger logger = LoggerFactory.getLogger(ModuleBuilder.class);
    private static final String DEFAULT_GROUP = "Miscellaneous";

    /** The type of this handler. */
    private Class<? extends Handler> type;

    /** The group this module belongs too. Defaults to {@link #DEFAULT_GROUP}.*/
    private String group;

    /** The user friendly name of this module. */
    private String name;

    /** The aliases for this module, this is how it can be accessed. */
    private Set<String> aliases;

    /** The optional help descriptor for this module. */
    private String help;

    /** If the help is publically visible. */
    private boolean isHidden;

    private Collection<CommandBuilder> commands;

    public ModuleBuilder(Class<? extends Handler> type) {
        this.type = type;
        aliases = new HashSet<>();
        commands = new ArrayList<>();
    }

    public void addCommand(CommandBuilder builder) {
        Set<String> existing = commands.stream()
            .flatMap((c) -> c.getAliases().stream())
            .collect(Collectors.toSet());

        for (String alias : aliases) {
            if (!existing.contains(alias))
                continue;

            String format = "Command `%s` contains alias `%s` which has already been registered by another command in module.";
            throw new IllegalStateException(String.format(format, builder.getName(), alias));
        }

        boolean multiDefaults = commands.stream()
            .anyMatch(CommandBuilder::isDefault);

        if (multiDefaults) {
            String format = ("Module `%s` contains multiple default commands.");
            throw new IllegalStateException(String.format(format, name));
        }

        commands.add(builder);
    }

    public MetaModule build(ContextLoader context) {
        if (group == null || group.isEmpty())
            group = DEFAULT_GROUP;

        MetaModule data = new MetaModule(type, this);

        for (MetaModule module : context.getModules()) {
            if (name.equalsIgnoreCase(module.getName()))
                throw new ConflictingModuleException("Module `%s` already exists within current context.", name);

            if (!Collections.disjoint(aliases, module.getAliases()))
                throw new ConflictingModuleException("Module `%s` has an alias which is already taken by another module.", name);

            Collection<String> staticAliases = module.getCommands().stream()
                .filter(MetaCommand::isStatic)
                .flatMap((c) -> c.getAliases().stream())
                .collect(Collectors.toList());

            if (!Collections.disjoint(aliases, staticAliases))
                throw new ConflictingModuleException("Module `%s` has an alias which is already taken by a static command.", name);
        }

        return data;
    }

    public Class<? extends Handler> getHandlerType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ModuleBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public ModuleBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public ModuleBuilder setAliases(String... aliases) {
        this.aliases.addAll(Stream.of(aliases)
                .map(String::toLowerCase).collect(Collectors.toSet()));

        return this;
    }

    public String getHelp() {
        return help;
    }

    public ModuleBuilder setHelp(String help) {
        this.help = help;
        return this;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public ModuleBuilder setHidden(boolean hidden) {
        isHidden = hidden;
        return this;
    }

    public Collection<CommandBuilder> getCommands() {
        return commands;
    }

    public ModuleBuilder setCommands(CommandBuilder... commands) {
        return setCommands(List.of(commands));
    }

    public ModuleBuilder setCommands(Collection<CommandBuilder> commands) {
        this.commands = commands;
        return this;
    }

    @Override
    public String toString() {
        return type.getSimpleName() + " | " + name;
    }

    @Override
    public Iterator<CommandBuilder> iterator() {
        return commands.iterator();
    }
}
