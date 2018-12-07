package com.elypia.commandler.metadata;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.CommandEvent;
import com.elypia.commandler.interfaces.ICommandEvent;
import org.slf4j.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class CommandData implements Comparable<CommandData> {

    /**
     * We use SLF4J for logging, be sure to include an implementation / binding
     * so you can configure warnings and other messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(CommandData.class);

    private ModulesContext context;

    /**
     * The parent {@link ModuleData} that this {@link CommandData}
     * belongs to.
     */
    protected ModuleData moduleData;

    /**
     * The type of module this command belongs too.
     */
    protected Class<? extends Handler> clazz;

    /**
     * The static data associated with this command.
     */
    protected Command command;

    /**
     * The actual method that is called when this command is performed.
     */
    protected Method method;

    /**
     * The parameters this command requires, this includes event parameters
     * which are not input by the user but instead handled by Commandler.
     */
    protected List<ParamData> paramData;

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
    private List<CommandData> overloads;

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

    /**
     * If this is an {@link Overload} to another command rather
     * than a command of itself.
     */
    private boolean isOverload;

    CommandData(ModuleData moduleData, Method method) {
        this(moduleData, method, null);
    }

    private CommandData(ModuleData moduleData, Method method, CommandData commandData) {
        this.moduleData = Objects.requireNonNull(moduleData);
        this.method = Objects.requireNonNull(method);
        this.context = moduleData.getContext();

        aliases = new HashSet<>();
        paramData = new ArrayList<>();

        command = method.getAnnotation(Command.class);
        isOverload = method.isAnnotationPresent(Overload.class);

        if (command != null && isOverload) {
            String moduleName = moduleData.getAnnotation().name();
            String typeName = moduleData.getClass().getName();
            String message = String.format("Command in module %s (%s) has both @Command and @Overload annotation. A command can only be one.", moduleName, typeName);

            throw new IllegalStateException(message);
        }

        if (command != null) {
            overloads = new ArrayList<>();
            parseAnnotations();
            parseAliases();
            parseParams();

            int id = command.id();
            if (id != Command.DEFAULT_ID) {
                for (Method m : moduleData.getModuleClass().getMethods()) {
                    Overload overload = m.getAnnotation(Overload.class);

                    if (overload != null && overload.value() == id)
                        overloads.add(new CommandData(moduleData, m, this));
                }
            }

            isPublic = !command.help().equals(Command.DEFAULT_HELP);
        } else {
            parseOverload(commandData);
        }
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
            String moduleName = moduleData.getAnnotation().name();
            String handlerName = moduleData.getModuleClass().getName();

            logger.warn("Command {} in module {} ({}) contains multiple aliases which are identical.", command.name(), moduleName, handlerName);
        }

        if (isStatic) {
            if (!Collections.disjoint(context.getAliases(), aliases)) {
                String commandName = command.name();
                String moduleName = moduleData.getAnnotation().name();
                String moduleType = moduleData.getModuleClass().getName();

                throw new IllegalStateException(String.format("Command %s in module %s (%s) contains a static alias which has already been registered by a previous module or static command.", commandName, moduleName, moduleType));
            }

            for (String in : aliases)
                context.getAliases().add(in);
        }
    }

    /**
     * Parses the annotations on this command or the parent module if appropriate.
     * Is the annotation is a commandValidator, adds it to the internal list of validators.
     * This is determined by if the annotation if a {@link ICommandValidator} is registered for this type. <br>
     * If the annotation is {@link Static} that sets this as a static command. <br>
     * If the annotation is {@link Default} that sets this as a default command. <br>
     *
     * Once the command is iterated, we search through any annotations on the {@link Handler}
     * to see if there are any default validation for commands in this module if not
     * specified at the command already.
     */
    protected void parseAnnotations() {
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            if (type == Static.class)
                isStatic = true;

            else if (type == Default.class)
                isDefault = true;
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
        inputRequired = checkParamLength(parameters, params.length);

        int offset = 0;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = null;

            if (ICommandEvent.class.isAssignableFrom(parameter.getType()))
                checkOffset(++offset);
            else
                param = params[i - offset];

            ParamData meta = new ParamData(this, param, parameter);
            paramData.add(meta);
        }
    }

    /**
     * This is only performed if the {@link CommandData}
     * is an {@link Overload}. Copies data from the parent
     * {@link CommandData} and applies the {@link Overload} to make it
     * fit as described. <br>
     *
     * <strong>Note:</strong> Use {@link Overload} where possible it
     * making similar commands to avoid having to redefine or copy and paste
     * meta-data already defined in another {@link Command}.
     *
     * @param commandData The parent {@link CommandData} that found this {@link Overload}.
     */ // ? This command is very similar to our existing methods, see if we can use them?
    protected void parseOverload(CommandData commandData) {
        command = commandData.command;

        List<ParamData> parentParams = commandData.paramData.stream().filter(o -> {
            return o.isInput();
        }).collect(Collectors.toList());

        Overload overload = method.getAnnotation(Overload.class);
        Param[] params = method.getAnnotationsByType(Param.class);
        List<String> order = new ArrayList<>(Arrays.asList(overload.params()));
        boolean inherit = order.size() == 1 && order.get(0).equals(Overload.INHERIT);

        if (inherit) {
            order.clear(); // ? Remove the inherit flag.

            parentParams.forEach(o -> order.add(o.getAnnotation().name()));

            for (Param param : params)
                order.add(param.name());
        }

        Parameter[] parameters = method.getParameters();
        inputRequired = checkParamLength(parameters, order.size());

        int offset = 0;

        outer:
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = null;

            if (CommandEvent.class.isAssignableFrom(parameter.getType()))
                checkOffset(++offset);
            else {
                String name = order.get(i - offset);

                for (int ii = 0; ii < parentParams.size(); ii++) {
                    ParamData parentParam = parentParams.get(ii);

                    if (name.equals(parentParam.getAnnotation().name())) {
                        paramData.add(parentParams.remove(ii));
                        continue outer;
                    }
                }

                for (Param p : params) {
                    if (name.equals(p.name()))
                        param = p;
                }
            }

            ParamData meta = new ParamData(this, param, parameter);
            paramData.add(meta);
        }
    }

    private int checkParamLength(Parameter[] parameters, int paramLength) {
        int inputRequired = (int)Arrays.stream(parameters).filter(o -> {
            return !ICommandEvent.class.isAssignableFrom(o.getType());
        }).count();

        if (inputRequired != paramLength) {
            String commandName = command.name();
            String moduleName = moduleData.getAnnotation().name();
            String typeName = moduleData.getModuleClass().getName();
            String message = String.format("Command %s in module %s (%s) doesn't contain the correct number of @Param annotations.", commandName, moduleName, typeName);

            throw new IllegalStateException(message);
        }

        return inputRequired;
    }

    private void checkOffset(int offset) {
        if (offset == 2) {
            String commandName = command.name();
            String moduleName = moduleData.getAnnotation().name();
            String typeName = moduleData.getModuleClass().getName();

            logger.warn("Command {} in module {} ({}) contains multiple Event parameters, there is no benefit to this.", commandName, moduleName, typeName);
        }
    }

    @Override
    public String toString() {
        List<ParamData> params = getInputParams();

        if (params.isEmpty())
            return "(0) None";

        StringJoiner itemJoiner = new StringJoiner(", ");

        for (ParamData param : params) {
            String name = param.getAnnotation().name();

            if (param.isList())
                itemJoiner.add("['" + name + "']");
            else
                itemJoiner.add("'" + name + "'");
        }

        return "(" + params.size() + ") " + itemJoiner.toString();
    }

    /**
     * @param input The input module by the user.
     * @return If this command contains an entry of that command.
     */
    public boolean performed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public ModuleData getModuleData() {
        return moduleData;
    }

    public Command getCommand() {
        return command;
    }

    public Method getMethod() {
        return method;
    }

    public List<ParamData> getParamData() {
        return Collections.unmodifiableList(paramData);
    }

    public List<ParamData> getInputParams() {
        return paramData.stream().filter(ParamData::isInput).collect(Collectors.toUnmodifiableList());
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public CommandData getOverload(int paramCount) {
        for (CommandData commandData : getOverloads(true)) {
            if (commandData.inputRequired == paramCount)
                return commandData;
        }

        return null;
    }

    /**
     * @return A list of overloads belonging to this command, exclusive
     *         of the base command itself.
     * @throws UnsupportedOperationException If this instance of a {@link CommandData}
     *         is already of an {@link Overload}.
     */
    public List<CommandData> getOverloads() {
        return getOverloads(false);
    }

    /**
     * @param includeCommand If to include the instance of the main command as well.
     * @return A list of overloads belonging to this command.
     * @throws UnsupportedOperationException If this instance of a {@link CommandData}
     *         is already of an {@link Overload}.
     */
    public List<CommandData> getOverloads(boolean includeCommand) {
        if (isOverload)
            throw new UnsupportedOperationException("Can't retrieve overloads from an overload.");

        List<CommandData> list = new ArrayList<>(overloads);

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

    public boolean isOverload() {
        return isOverload;
    }

    public int getInputRequired() {
        return inputRequired;
    }

    @Override
    public int compareTo(CommandData o) {
        return command.name().compareToIgnoreCase(o.command.name());
    }
}
