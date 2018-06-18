package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.validation.ICommandValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class MetaCommand {

    /**
     * The parent {@link Commandler} instance that spawned this object.
     */

    private Commandler commandler;

    /**
     * The {@link Module} {@link CommandHandler (Command Handler)} this command
     * belongs to.
     */

    private CommandHandler handler;

    /**
     * The type of class the parent {@link #handler Command Handler} belongs to.
     */

    private Class<? extends CommandHandler> clazz;

    /**
     * The {@link MetaModule meta data} of the parent {@link #handler}
     * that spawned this {@link MetaCommand}.
     */

    private MetaModule metaModule;

    /**
     * The {@link Command} annotation associated with this command. <br>
     * This is what defines a method as a command in a Command Handler.
     */

    private Command command;

    /**
     * The method this is pulling metadata from.
     */

    private Method method;

    /**
     * A list of all validators associated with this command.
     * A validator is dictated by whether the annotation itself
     * has the {@link Validation} annotation.
     */

    private Map<Class<? extends Annotation>, MetaValidator> validators;

    /**
     * A list of unique aliases for this command. If the command
     * had duplicate aliases, they are filtered out.
     */

    private Set<String> aliases;

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

    /**
     * A list of any {@link MetaParam params} this command required to execute.
     */

    private List<MetaParam> metaParams;

    /**
     * The amount of input required to perform this command.
     */

    private int inputRequired;

    private Collection<MetaCommand> overloads;

    /**
     * Create a wrapper around this the command for the convinience of accessing
     * information around it.
     *
     * @param module The parent module that this command belongs to.
     * @param method The method this command executes.
     * @return A wrapper around this command.
     */

    protected static MetaCommand of(MetaModule module, Method method) {
        return new MetaCommand(null, module, method);
    }

    protected static MetaCommand of(MetaCommand metaCommand, MetaModule module, Method method) {
        return new MetaCommand(metaCommand, module, method);
    }

    private MetaCommand(MetaCommand metaCommand, MetaModule metaModule, Method method) {
        this.metaModule = Objects.requireNonNull(metaModule);
        this.method = Objects.requireNonNull(method);
        commandler = metaModule.getCommandler();

        clazz = metaModule.getHandlerType();

        if (metaCommand == null) {
            command = method.getAnnotation(Command.class);
            parseAliases();
        } else {
            command = metaCommand.getCommand();
        }

        parseParameters();
        parseAnnotations();

        if (isStatic) {
            if (!Collections.disjoint(commandler.getRootAlises(), aliases)) {
                String format = "Command %s in module %s (%s) contains a static alias which has already been registered by a previous module or static command.";
                String message = String.format(format, command.name(), metaModule.getModule().name(), clazz.getName());
                throw new RecursiveAliasException(message);
            }

            commandler.getRootAlises().addAll(aliases);
        }

        handler = metaModule.getHandler();
        isPublic = !command.help().equals("");
        overloads = new ArrayList<>();
    }

    /**
     * Parses the aliases from the module, converts them all to lower case
     * and stores them an a list in this class to save us from converting the aliases
     * to lower case everytime we want to compare them. <br>
     * Prints a warning should a command have multiple identical aliases; duplicates
     * are filtered out.
     */

    private void parseAliases() {
        aliases = new HashSet<>();

        for (String alias : command.aliases())
            aliases.add(alias.toLowerCase());

        if (aliases.size() != command.aliases().length) {
            String format = "Command %s in module %s (%s) contains multiple aliases which are identical.\n";
            System.err.printf(format, command.name(), metaModule.getModule().name(), clazz.getName());
        }
    }

    /**
     * Parses the annotations on this command or the parent module if appropriate.
     * Is the annotation is a validator, adds it to the internal list of validators.
     * This is determined by if the annotation has the {@link Validation} annotation. <br>
     * If the annotation is {@link Static} that sets this as a static command. <br>
     * If the annotation is {@link Default} that sets this as a default command. <br>
     *
     * Once the command is iterated, we search through any annotations on the {@link CommandHandler}
     * to see if there are any default validation for commands in this module if not
     * specified at the command already.
     */

    private void parseAnnotations() {
        validators = new HashMap<>();

        for (Annotation annotation : method.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            if (type.isAnnotationPresent(Validation.class))
                validators.put(type, MetaValidator.of(annotation));
            else if (type == Static.class)
                isStatic = true;
            else if (type == Default.class)
                isDefault = true;
        }

        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            if (!validators.containsKey(type) && type.isAnnotationPresent(Validation.class))
                validators.put(type, MetaValidator.of(annotation));
        }
    }

    /**
     * Parses all parameters of the command method and any {@link Param} annotations
     * to ensure they sync up and match according to the standards Commandler expects. <br>
     * This may print warning messages if a single method askes for {@link MessageEvent} multiple
     * times as there are <strong>no</strong> benefit to require this parameter more than once.
     *
     * @throws MalformedCommandException If there isn't a param annotation for every parameter in the method.
     */

    private void parseParameters() {
        metaParams = new ArrayList<>();
        Param[] params = method.getAnnotationsByType(Param.class);
        Parameter[] parameters = method.getParameters();

        if (Arrays.stream(parameters).filter(o -> o.getType() != MessageEvent.class).count() != params.length) {
            String format = "Command %s in module %s (%s) doesn't contain the correct number of @Param annotations.";
            String message = String.format(format, command.name(), metaModule.getModule().name(), clazz.getName());
            throw new MalformedCommandException(message);
        }

        int offset = 0;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = null;

            if (parameter.getType() != MessageEvent.class)
                param = params[i - offset];

            else if (++offset == 2)
                System.err.printf("Command %s in module %s (%s) contains multiple MessageEvent parameters, there is no benefit to this.\n", command.name(), metaModule.getModule().name(), clazz.getName());

            MetaParam meta = MetaParam.of(this, parameter, param);
            metaParams.add(meta);
        }

        inputRequired = (int)metaParams.stream().filter(MetaParam::isInput).count();
    }

    /**
     * @param input The input module by the user.
     * @return If this command contains an entry of that command.
     */

    public boolean hasPerformed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    /**
     * This will only return any validators that may have been registered at the time
     * this method is called, even if the @ValidatorAnnotation is found, a pairing
     * {@link ICommandValidator} must be registred via {@link Commandler#registerValidator(Class, ICommandValidator)}.
     *
     * @return A list of validators associated with this {@link Command}.
     */

    public Map<MetaValidator, ICommandValidator> getValidators() {
        Map<MetaValidator, ICommandValidator> validatorMap = new HashMap<>();

        commandler.getDispatcher().getValidator().getCommandValidators().forEach((type, validator) -> {
            validators.forEach((typeII, metaCommandValidator) -> {
                if (type == typeII)
                    validatorMap.put(metaCommandValidator, validator);
            });
        });

        return validatorMap;
    }

    public void registerOverload(MetaCommand metaCommand) {
        overloads.add(metaCommand);
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public Class<? extends CommandHandler> getHandlerType() {
        return clazz;
    }

    public MetaModule getMetaModule() {
        return metaModule;
    }

    public Command getCommand() {
        return command;
    }

    public Method getMethod() {
        return method;
    }

    public Set<String> getAliases() {
        return aliases;
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

    public List<MetaParam> getMetaParams() {
        return metaParams;
    }

    public int getInputRequired() {
        return inputRequired;
    }

    public Collection<MetaCommand> getOverloads() {
        return overloads;
    }
}
