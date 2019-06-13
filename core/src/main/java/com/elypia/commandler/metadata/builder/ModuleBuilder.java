package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.Handler;
import com.elypia.commandler.exceptions.ConflictingModuleException;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.data.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.*;

public class ModuleBuilder implements Iterable<CommandBuilder> {

    private static final Logger logger = LoggerFactory.getLogger(ModuleBuilder.class);
    private static final String DEFAULT_GROUP = "Miscellaneous";

    private Class<? extends Handler> clazz;
    private String name;
    private String group;
    private Set<String> aliases;
    private String help;
    private boolean isHidden;

    private Collection<CommandBuilder> commands;

    public ModuleBuilder(Class<? extends Handler> clazz) {
        this.clazz = clazz;
        aliases = new HashSet<>();
        commands = new ArrayList<>();
    }

    public ModuleBuilder addAliases(String... aliases) {
        this.aliases.addAll(Stream.of(aliases)
            .map(String::toLowerCase).collect(Collectors.toSet()));

        return this;
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

        MetaModule data = new MetaModule(clazz, this);

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

    public Class<? extends Handler> getHandlerClass() {
        return clazz;
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

    @Override
    public String toString() {
        return clazz.getSimpleName() + " | " + name;
    }

    @Override
    public Iterator<CommandBuilder> iterator() {
        return commands.iterator();
    }
}
