package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.impl.IHandler;
import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MetaModule<C, E, M> implements Comparable<MetaModule> {

    private static final Logger logger = LoggerFactory.getLogger(MetaModule.class);

    /**
     * Parent {@link Commandler} object this {@link MetaModule} is registered too.
     */

    private Commandler<C, E, M> commandler;

    /**
     * The {@link IHandler} this {@link MetaModule} is describing.
     */

    private IHandler handler;

    /**
     * The {@link Class} type of the {@link IHandler} this {@link MetaModule} is describing.
     */

    private Class<? extends IHandler> clazz;

    /**
     * The metadata associated with this module.
     */

    private Module module;

    /**
     * Does this module have a description. If the module doesn't specify a
     * description, it may be hidden from help commands and pages.
     */

    private boolean isPublic;

    /**
     * A list of collected aliases from this module. This is used to compare
     * any user input too as we convert all aliases to lower case in advance.
     */

    private Set<String> aliases;

    /**
     * A list of {@link MetaCommand} that were created inside the {@link IHandler}.
     */

    private List<MetaCommand<C, E, M>> metaCommands;

    /**
     * The {@link Default} command for this module. This value
     * can be null if no default command is specified.
     */

    private MetaCommand<C, E, M> defaultCommand;

    public <T extends IHandler<C, E, M>> MetaModule(Commandler<C, E, M> commandler, T t) {
        this.commandler = Objects.requireNonNull(commandler);
        handler = Objects.requireNonNull(t);
        clazz = t.getClass();
        module = clazz.getAnnotation(Module.class);

        if (module == null)
            throw new MalformedModuleException(String.format("Command handler %s isn't annotated with the Module annotation.", clazz.getName()));

        parseAliases();
        parseMethods();

        isPublic = !module.hidden();
    }

    /**
     * Parse aliases from this module, this creates a list of aliases.
     * All aliases on the module are converted to lower case and added to this.
     * If find duplicates, we continue filter them however warn the user as this
     * shouldn't be the case. <br>
     * Should an alias be registered that was already registed by another module,
     * we throw an exception as this is considered a malformed command.
     * If everything checks out alright, we add all the aliases to the list
     * of {@link Commandler#roots root aliases} and reserve these so other
     * modules of static commands can't try consume them.
     *
     * @throws RecursiveAliasException If this module has an alias which was already registered by
     * another module or static command.
     */

    private void parseAliases() {
        aliases = new HashSet<>();

        for (String alias : module.aliases())
            aliases.add(alias.toLowerCase());

        if (aliases.size() != module.aliases().length)
            logger.warn("Module {} ({}) contains multiple aliases which are identical.", module.name(), clazz.getName());

        if (Collections.disjoint(commandler.getRoots().keySet(), aliases)) {
            for (String in : aliases)
                commandler.getRoots().put(in, this);
        }
        else
            throw new RecursiveAliasException(String.format("Module %s contains an alias which has already been registered by a previous module or static command.", module.name()));
    }

    /**
     * Parses the methods in this Module and creates {@link MetaCommand} instances
     * out of methods with the {@link Command} annotation. <br>
     * This method ensures only one command in this module is a {@link Default default} command
     * if any, and validates all the underlying Commands that are created before adding their aliases
     * to the global list and utlimatly adding the command as valid. <br>
     * <strong>If <em>ANY</em> commands are invalid, the entire module will fail.</strong>
     *
     * @throws MalformedModuleException When module specified more than one command with the {@link Default} annotation.
     */

    private void parseMethods() {
        metaCommands = new ArrayList<>();

        Set<String> commandAliases = new HashSet<>();
        int defaultCommands = 0;

        Method[] methods = clazz.getMethods();
        methods = Arrays.stream(methods).filter(m -> m.isAnnotationPresent(Command.class)).toArray(Method[]::new);

        for (Method method : methods) {
            MetaCommand metaCommand = MetaCommand.of(this, method);

            if (metaCommand.isDefault()) {
                if (++defaultCommands == 2)
                    throw new MalformedModuleException(String.format("Module %s (%s) contains multiple default commands, modules may only have a single default.", module.name(), clazz.getName()));
            }

            if (Collections.disjoint(commandAliases, metaCommand.getAliases()))
                commandAliases.addAll(metaCommand.getAliases());
            else
                throw new RecursiveAliasException(String.format("Command %s in module %s (%s) contains an alias which has already been registered by a previous command in this module.", metaCommand.getCommand().name(), module.name(), clazz.getName()));

            metaCommands.add(metaCommand);

            if (metaCommand.isDefault())
                defaultCommand = metaCommand;
        }

        Collections.sort(metaCommands);
    }

    /**
     * @param input The input module by the user.
     * @return If this module contains an entry of that module.
     */

    public boolean hasPerformed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public List<MetaCommand> getPublicCommands() {
        return metaCommands.stream().filter(MetaCommand::isPublic).collect(Collectors.toList());
    }

    /**
     * @return A list of all {@link Static} commands in the module.
     */

    public List<MetaCommand> getStaticCommands() {
        return metaCommands.stream().filter(MetaCommand::isStatic).collect(Collectors.toList());
    }

    public MetaCommand<C, E, M> getCommand(String input) {
        input = input.toLowerCase();

        for (MetaCommand<C, E, M> metaCommand : metaCommands) {
            if (metaCommand.getAliases().contains(input))
                return metaCommand;
        }

        return null;
    }

    public Commandler<C, E, M> getCommandler() {
        return commandler;
    }

    public IHandler<C, E, M> getHandler() {
        return handler;
    }

    public Class<? extends IHandler> getHandlerType() {
        return clazz;
    }

    public Module getModule() {
        return module;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Collection<String> getAliases() {
        return aliases;
    }

    public Collection<MetaCommand<C, E, M>> getMetaCommands() {
        return metaCommands;
    }

    public MetaCommand<C, E, M> getDefaultCommand() {
        return defaultCommand;
    }

    @Override
    public int compareTo(MetaModule o) {
        return module.name().compareToIgnoreCase(o.module.name());
    }
}
