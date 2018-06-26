package com.elypia.commandler.metadata;

import com.elypia.commandler.Utils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.*;
import com.elypia.commandler.events.*;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.modules.CommandHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class MetaCommand extends AbstractMetaCommand implements Comparable<MetaCommand> {

    /**
     * This is the ID of the command as managed used internally by Commandler.
     */

    private int id;

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

    private List<MetaOverload> overloads;

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
     * If this command completely disregards any annotations that were annotated
     * at the module level.
     */

    private boolean ignoresGlobal;

    /**
     * Create a wrapper around this the command for the convinience of accessing
     * information around it.
     *
     * @param module The parent module that this command belongs to.
     * @param method The method this command executes.
     * @return A wrapper around this command.
     */

    protected static MetaCommand of(MetaModule module, Method method) {
        return new MetaCommand(module, method);
    }

    private MetaCommand(MetaModule metaModule, Method method) {
        super(metaModule, method);
        command = method.getAnnotation(Command.class);
        id = command.id();

        parseAliases();

        if (id != -1) {
            commandler.getCommands().put(id, this);
            parseOverloads();
        }
        if (isStatic) {
            if (!Collections.disjoint(commandler.getRootAlises(), aliases))
                throw new RecursiveAliasException(String.format("Command %s in module %s (%s) contains a static alias which has already been registered by a previous module or static command.", command.name(), metaModule.getModule().name(), metaModule.getHandlerType().getName()));

            commandler.getRootAlises().addAll(aliases);
        }

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

        if (aliases.size() != command.aliases().length)
            Utils.log("Command %s in module %s (%s) contains multiple aliases which are identical.", command.name(), metaModule.getModule().name(), clazz.getName());
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

    @Override
    protected void parseAnnotations() {
        validationAnnotations = new HashMap<>();

        for (Annotation annotation : method.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            if (type.isAnnotationPresent(Validation.class))
                validationAnnotations.put(type, annotation);
            else if (type == Static.class)
                isStatic = true;
            else if (type == Default.class)
                isDefault = true;
            else if (type == IgnoreGlobal.class)
                ignoresGlobal = true;
        }

        if (!ignoresGlobal) {
            for (Annotation annotation : metaModule.getHandlerType().getDeclaredAnnotations()) {
                Class<? extends Annotation> type = annotation.annotationType();

                if (validationAnnotations.containsKey(type) && type.isAnnotationPresent(Validation.class))
                    validationAnnotations.put(type, annotation);
            }
        }
    }

    /**
     * Parses all parameters of the command method and any {@link Param} annotations
     * to ensure they sync up and match according to the standards Commandler expects. <br>
     * This may print warning messages if a single method askes for {@link CommandEvent} multiple
     * times as there are <strong>no</strong> benefit to require this parameter more than once.
     *
     * @throws MalformedCommandException If there isn't a param annotation for every parameter in the method.
     */

    @Override
    protected void parseParams() {
        metaParams = new ArrayList<>();

        Param[] params = method.getAnnotationsByType(Param.class);
        Parameter[] parameters = method.getParameters();
        inputRequired = (int)Arrays.stream(parameters).filter(o -> o.getType() != CommandEvent.class).count();

        if (inputRequired != params.length)
            throw new MalformedCommandException(String.format("Command %s in module %s (%s) doesn't contain the correct number of @Param annotations.", command.name(), metaModule.getModule().name(), clazz.getName()));

        int offset = 0;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = null;

            if (parameter.getType() != CommandEvent.class)
                param = params[i - offset];
            else if (++offset == 2)
                Utils.log("Command %s in module %s (%s) contains multiple MessageEvent parameters, there is no benefit to this.", command.name(), metaModule.getModule().name(), clazz.getName());

            MetaParam meta = MetaParam.of(this, param, parameter);
            metaParams.add(meta);
        }
    }

    private void parseOverloads() {
        for (Method method : metaModule.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Overload.class)) {
                Overload overload = method.getAnnotation(Overload.class);

                if (id == overload.value())
                    overloads.add(new MetaOverload(this, method, overload));
            }
        }
    }

    /**
     * @param input The input module by the user.
     * @return If this command contains an entry of that command.
     */

    public boolean hasPerformed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public List<MetaOverload> getOverloads() {
        return overloads;
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

    @Override
    public int compareTo(MetaCommand o) {
        return command.name().compareToIgnoreCase(o.command.name());
    }
}
