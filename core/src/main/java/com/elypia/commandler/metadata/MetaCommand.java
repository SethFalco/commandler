package com.elypia.commandler.metadata;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.*;
import com.elypia.commandler.impl.*;
import org.slf4j.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class MetaCommand<C, E, M> implements Comparable<MetaCommand> {

    /**
     * This may occur when a {@link Static} command attempts to register
     * an alias which has already been registered by a previous module or static command.
     */

    private static final String RECURSIVE_ALIAS = "Command %s in module %s (%s) contains a static alias which has already been registered by a previous module or static command.";

    /**
     * This may be logged if a command contains multiple aliases that
     * are have the same value (non-case sensitive). There
     * is no benefit to this as the other aliases are just ignored.
     */

    private static final String DUPLICATE_ALIAS = "Command {} in module {} ({}) contains multiple aliases which are identical.";

    /**
     * This may be thrown if the wrong number of {@link Param} annotations
     * are found for the amount of input required for the command. Help
     * should be defined for every argument in the {@link Command}.
     */

    private static final String PARAM_COUNT_MISMATCH = "Command %s in module %s (%s) doesn't contain the correct number of @Param annotations.";

    /**
     * This may be logged if the parameters contain multiple instances of the
     * event. There is no benefit to this as it's just instances of the same
     * event.
     */

    private static final String DUPLICATE_EVENT_PARAM = "Command {} in module {} ({}) contains multiple Event parameters, there is no benefit to this.";

    /**
     * We use SLF4J for logging, be sure to include an implementation / binding
     * so you can configure warnings and other messages.
     */

    private static final Logger logger = LoggerFactory.getLogger(MetaCommand.class);

    /**
     * The {@link Commandler} instance that the parent
     * {@link #metaModule} was registered with.
     */

    protected Commandler<C, E, M> commandler;

    /**
     * The parent {@link MetaModule} that this {@link MetaCommand}
     * belongs to.
     */

    protected MetaModule<C, E, M> metaModule;

    /**
     * The command handler this {@link MetaCommand} belongs to.
     */

    protected IHandler<C, E, M> handler;

    /**
     * The static data associated with this command.
     */

    protected Command command;

    /**
     * The actual method that is called when this command is performed.
     */

    protected Method method;

    /**
     * All validation that will be performed on this {@link Command}
     * when called.
     */

    protected Map<MetaValidator, ICommandValidator> validators;

    /**
     * The parameters this command requires, this includes event parameters
     * which are not input by the user but instead handled by Commandler.
     */

    protected List<MetaParam> metaParams;

    /**
     * A list of unique aliases for this command. If the command
     * had duplicate aliases, they are filtered out.
     */

    private Set<String> aliases;

    /**
     * A list of overloads that are children of this command.
     * These are alternative methods of doing this command, and adopt
     * meta data from here.
     */

    private List<MetaCommand<C, E, M>> overloads;

    /**
     * Is this a {@link Static} command. <br>
     * Static commands can be performed without specifying the
     * module it belongs too, despite this it still belongs to the module.
     */

    private boolean isStatic;

    /**
     * Is this a {@link Default} command. <br>
     * Default commands are what we assume if the user didn't specify a command
     * or the next input after the module name doesn't fit the name requirement
     * or alias of any commands in the alias.
     */

    private boolean isDefault;

    /**
     * Should this command be displayed in help documentation when a user queries
     * it. <br>
     * If no help is specified on a command, it is considered a private command.
     */

    private boolean isPublic;

    private int inputRequired;

    MetaCommand(MetaModule<C, E, M> metaModule, Method method) {
        this(metaModule, method, null);
    }

    private MetaCommand(MetaModule<C, E, M> metaModule, Method method, MetaCommand<C, E, M> metaCommand) {
        this.metaModule = Objects.requireNonNull(metaModule);
        this.method = Objects.requireNonNull(method);
        commandler = metaModule.getCommandler();
        handler = metaModule.getHandler();
        command = method.getAnnotation(Command.class);

        aliases = new HashSet<>();
        metaParams = new ArrayList<>();
        validators = new HashMap<>();
        overloads = new ArrayList<>();

        parseAnnotations(); // ? Parse annotations first as this can affect how we parse aliases.
        parseAliases();
        parseParams();

        isPublic = !command.help().equals(Command.DEFAULT_HELP);
    }

    /**
     * Parses the aliases from the module, converts them all to lower case
     * and stores them an a list in this class to save us from converting the aliases
     * to lower case everytime we want to compare them. <br>
     * Prints a warning should a command have multiple identical aliases; duplicates
     * are filtered out.
     */

    private void parseAliases() {
        String[] commandAlliases = command.aliases();

        for (String alias : commandAlliases)
            aliases.add(alias.toLowerCase());

        if (aliases.size() != commandAlliases.length) {
            String moduleName = metaModule.getModule().name();
            String handlerName = handler.getClass().getName();

            logger.warn(DUPLICATE_ALIAS, command.name(), moduleName, handlerName);
        }

        if (isStatic) {
            if (!Collections.disjoint(commandler.getRoots().keySet(), aliases)) {
                String commandName = command.name();
                String moduleName = metaModule.getModule().name();
                String moduleType = metaModule.getHandler().getClass().getName();

                throw new IllegalStateException(String.format(RECURSIVE_ALIAS, commandName, moduleName, moduleType));
            }

            for (String in : aliases)
                commandler.getRoots().put(in, metaModule);
        }
    }

    /**
     * Parses the annotations on this command or the parent module if appropriate.
     * Is the annotation is a validator, adds it to the internal list of validators.
     * This is determined by if the annotation has the {@link Validation} annotation. <br>
     * If the annotation is {@link Static} that sets this as a static command. <br>
     * If the annotation is {@link Default} that sets this as a default command. <br>
     *
     * Once the command is iterated, we search through any annotations on the {@link IHandler}
     * to see if there are any default validation for commands in this module if not
     * specified at the command already unless the {@link Ignore} annotation is present.
     */

    protected void parseAnnotations() {
        List<Class<? extends Annotation>> registered = new ArrayList<>();
        boolean isIgnore = false;

        for (Annotation annotation : method.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            if (type == Static.class)
                isStatic = true;
            else if (type == Default.class)
                isDefault = true;
            else if (type == Ignore.class)
                isIgnore = true;
            else if (type.isAnnotationPresent(Validation.class)) {
                MetaValidator metaValidator = new MetaValidator(annotation);
                ICommandValidator validator = commandler.getValidator().getCommandValidator(type);
                registered.add(type);
                validators.put(metaValidator, validator);

            }
        }

        if (!isIgnore) {
            for (Annotation annotation : metaModule.getHandler().getClass().getDeclaredAnnotations()) {
                Class<? extends Annotation> type = annotation.annotationType();

                if (!registered.contains(type) && type.isAnnotationPresent(Validation.class)) {
                    MetaValidator metaValidator = new MetaValidator(annotation);
                    ICommandValidator validator = commandler.getValidator().getCommandValidator(type);

                    validators.put(metaValidator, validator);
                }
            }
        }
    }

    /**
     * Parses all parameters of the command method and any {@link Param} annotations
     * to ensure they sync up and match according to the standards Commandler expects. <br>
     * This may print warning messages if a single method askes for {@link CommandEvent} multiple
     * times as there are <strong>no</strong> benefit to require this parameter more than once.
     *
     * @throws IllegalStateException If there isn't a param annotation for every parameter in the method.
     */

    protected void parseParams() {
        Param[] params = method.getAnnotationsByType(Param.class);
        Parameter[] parameters = method.getParameters();
        inputRequired = (int)Arrays.stream(parameters).filter(o -> {
            return !CommandEvent.class.isAssignableFrom(o.getType());
        }).count();

        if (inputRequired != params.length) {
            String commandName = command.name();
            String moduleName = metaModule.getModule().name();
            String typeName = handler.getClass().getName();
            String message = String.format(PARAM_COUNT_MISMATCH, commandName, moduleName, typeName);

            throw new IllegalStateException(message);
        }

        int offset = 0;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = null;

            if (!CommandEvent.class.isAssignableFrom(parameter.getType()))
                param = params[i - offset];
            else if (++offset == 2) {
                String commandName = command.name();
                String moduleName = metaModule.getModule().name();
                String typeName = handler.getClass().getName();

                logger.warn(DUPLICATE_EVENT_PARAM, commandName, moduleName, typeName);
            }

            MetaParam meta = new MetaParam(this, param, parameter);
            metaParams.add(meta);
        }
    }

    /**
     * @param input The input module by the user.
     * @return If this command contains an entry of that command.
     */

    public boolean performed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public Commandler<C, E, M> getCommandler() {
        return commandler;
    }

    public MetaModule<C, E, M> getMetaModule() {
        return metaModule;
    }

    public IHandler<C, E, M> getHandler() {
        return handler;
    }

    public Command getCommand() {
        return command;
    }

    public Method getMethod() {
        return method;
    }

    public List<MetaParam> getMetaParams() {
        return Collections.unmodifiableList(metaParams);
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public List<MetaCommand<C, E, M>> getOverloads() {
        return getOverloads(false);
    }

    public List<MetaCommand<C, E, M>> getOverloads(boolean includeCommand) {
        List<MetaCommand<C, E, M>> list = new ArrayList<>(overloads);

        if (includeCommand)
            list.add(0, this);

        return Collections.unmodifiableList(list);
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public int getInputRequired() {
        return inputRequired;
    }

    @Override
    public int compareTo(MetaCommand o) {
        return command.name().compareToIgnoreCase(o.command.name());
    }
}
