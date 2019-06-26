package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.exceptions.ConflictingModuleException;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.metadata.data.*;
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

    private Collection<CommandBuilder> commandBuilders;

    public ModuleBuilder(Class<? extends Handler> type) {
        this.type = type;
        aliases = new HashSet<>();
        commandBuilders = new ArrayList<>();
    }

    public void addCommand(CommandBuilder builder) {
        Set<String> existing = commandBuilders.stream()
            .flatMap((c) -> c.getAliases().stream())
            .collect(Collectors.toSet());

        for (String alias : aliases) {
            if (!existing.contains(alias))
                continue;

            String format = "Command `%s` contains alias `%s` which has already been registered by another command in module.";
            throw new IllegalStateException(String.format(format, builder.getName(), alias));
        }

        boolean multiDefaults = commandBuilders.stream()
            .anyMatch(CommandBuilder::isDefault);

        if (multiDefaults) {
            String format = ("Module `%s` contains multiple default commandBuilders.");
            throw new IllegalStateException(String.format(format, name));
        }

        commandBuilders.add(builder);
    }

    public MetaModule build(MetaModule... modules) {
        if (group == null)
            group = DEFAULT_GROUP;

        List<MetaCommand> metaCommands = this.commandBuilders.stream()
            .map((m) -> m.build(this))
            .collect(Collectors.toUnmodifiableList());

        MetaModule data = new MetaModule(type, group, name, aliases, help, isHidden, metaCommands);

        for (MetaModule module : modules) {
            if (name.equalsIgnoreCase(module.getName()))
                throw new ConflictingModuleException("Module `" + name + "` already exists within current context.");

            if (!Collections.disjoint(aliases, module.getAliases()))
                throw new ConflictingModuleException("Module `" + name + "` has an alias which is already taken by another module.");

            Collection<String> staticAliases = module.getStaticCommands().stream()
                .flatMap((c) -> c.getAliases().stream())
                .collect(Collectors.toList());

            if (!Collections.disjoint(aliases, staticAliases))
                throw new ConflictingModuleException("Module `" + name + "` has an alias which is already taken by a static command.");
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

    public Collection<CommandBuilder> getCommandBuilders() {
        return commandBuilders;
    }

    public ModuleBuilder setCommandBuilders(CommandBuilder... commandBuilders) {
        return setCommands(List.of(commandBuilders));
    }

    public ModuleBuilder setCommands(Collection<CommandBuilder> commands) {
        this.commandBuilders = commands;
        return this;
    }

    @Override
    public String toString() {
        return type.getSimpleName() + " | " + name;
    }

    @Override
    public Iterator<CommandBuilder> iterator() {
        return commandBuilders.iterator();
    }
}
