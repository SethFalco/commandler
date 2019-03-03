package com.elypia.commandler.metadata.data;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.builder.*;
import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;

public class CommandData implements Comparable<CommandData> {

    /**
     * We use SLF4J for logging, be sure to include an implementation / binding
     * so you can configure warnings and other messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(CommandData.class);

    /**
     * The type of module this command belongs too.
     */
    private Class<? extends Handler> clazz;

    /**
     * The actual method that is called when this command is performed.
     */
    private Method method;

    private String name;

    /**
     * A list of unique aliases for this command. If the command
     * had duplicate aliases, they are filtered out.
     */
    private Set<String> aliases;

    private String help;

    private boolean isHidden;

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
     * The parameters this command requires.
     */
    private List<ParamData> params;

    /**
     * A list of overloads that are children of this command.
     * These are alternative methods of doing this command, and adopt
     * meta data from here.
     */
    private List<CommandData> overloads;

    /**
     * If this is an {@link Overload} to another command rather
     * than a command of itself.
     */
    private boolean isOverload;

    public CommandData(CommandBuilder builder) {
        Objects.requireNonNull(builder);
        this.method = Objects.requireNonNull(builder.getMethod());
        this.name = Objects.requireNonNull(builder.getName());
        this.help = Objects.requireNonNull(builder.getHelp());
        this.aliases = Objects.requireNonNull(builder.getAliases());
        this.isHidden = builder.isHidden();
        this.isStatic = builder.isStatic();
        this.isDefault = builder.isDefault();

        params = new ArrayList<>();

        for (ParamBuilder param : builder)
            params.add(param.build(this));

        // Temp commented, overloads will not work!!!

//        if (command != null && isOverload) {
//            String moduleName = moduleData.getAnnotation().name();
//            String typeName = moduleData.getClass().getName();
//            String message = String.format("Command in module %s (%s) has both @Command and @Overload annotation. A command can only be one.", moduleName, typeName);
//
//            throw new IllegalStateException(message);
//        }
//
//        if (command  != null) {
//            for (Method m : moduleData.getModuleClass().getMethods()) {
//                Overload overload = m.getAnnotation(Overload.class);
//
//                if (overload != null && overload.value().equals(id))
//                    overloads.add(new CommandData(moduleData, m, this));
//            }
//        }
    }
//
//    /**
//     * This is only performed if the {@link CommandData}
//     * is an {@link Overload}. Copies data from the parent
//     * {@link CommandData} and applies the {@link Overload} to make it
//     * fit as described. <br>
//     *
//     * <strong>Note:</strong> Use {@link Overload} where possible it
//     * making similar commands to avoid having to redefine or copy and paste
//     * meta-data already defined in another {@link Command}.
//     *
//     * @param commandData The parent {@link CommandData} that owns this {@link Overload}.
//     */
//    protected void parseOverload(CommandData commandData) {
//        command = commandData.command;
//
//        List<ParamData> parentParams = commandData.paramData.stream()
//            .filter(ParamData::isInput)
//            .collect(Collectors.toList());
//
//        Parameter[] parameters = method.getParameters();
//        inputRequired = getParamLength(parameters);
//
//        paramData = new ArrayList<>(parentParams);
//
//        int offset = 0;
//
//        for (Parameter parameter : parameters) {
//            if (CommandlerEvent.class.isAssignableFrom(parameter.getType()))
//                checkOffset(++offset);
//            else {
//                if (parameter.isAnnotationPresent(Param.class)) {
//                    Param param = parameter.getAnnotation(Param.class);
//                    ParamData meta = new ParamData(this, param, parameter);
//                    paramData.add(meta);
//                }
//            }
//        }
//    }
//
//    private int getParamLength(Parameter[] parameters) {
//        return (int)Arrays.stream(parameters).filter(o -> {
//            return !CommandlerEvent.class.isAssignableFrom(o.getType());
//        }).count();
//    }
//
//    private void checkOffset(int offset) {
//        if (offset == 2) {
//            String commandName = command.id();
//            String moduleName = moduleData.getAnnotation().name();
//            String typeName = moduleData.getModuleClass().getName();
//
//            logger.warn("Command {} in module {} ({}) contains multiple Event parameters, there is no benefit to this.", commandName, moduleName, typeName);
//        }
//    }

    /**
     * @param input The input module by the user.
     * @return If this command contains an entry of that command.
     */
    public boolean performed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public Method getMethod() {
        return method;
    }

    public List<ParamData> getParamData() {
        return Collections.unmodifiableList(params);
    }

    public Set<String> getAliases() {
        return aliases;
    }

//    public CommandData getOverload(int paramCount) {
//        for (CommandData commandData : getOverloads()) {
//            if (commandData.inputRequired == paramCount)
//                return commandData;
//        }
//
//        return null;
//    }

    /**
     * @return A list of overloads belonging to this command, exclusive
     *         of the base command itself.
     * @throws UnsupportedOperationException If this instance of a {@link CommandData}
     *         is already of an {@link Overload}.
     */
    public List<CommandData> getOverloads() {
        return getOverloads(true);
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

    public boolean isOverload() {
        return isOverload;
    }

    @Override
    public String toString() {
        return name + " " + toParamsString();
    }

    public String toParamsString() {
        if (params == null)
            return "Uninitialized";

        if (params.isEmpty())
            return "(0) None";

        StringJoiner itemJoiner = new StringJoiner(", ");

        for (ParamData param : params) {
            String name = param.getName();

            if (param.isList())
                itemJoiner.add("['" + name + "']");
            else
                itemJoiner.add("'" + name + "'");
        }

        return "(" + params.size() + ") " + itemJoiner.toString();
    }

    @Override
    public int compareTo(CommandData o) {
        return name.compareToIgnoreCase(o.name);
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public boolean isHidden() {
        return isHidden;
    }
}
