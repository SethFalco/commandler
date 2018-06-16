package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public final class CommandUtils {

    /**
     * Get the commands this handler holds which are relevent to the
     * current MessageEvent. <br>
     * Eg <strong>&gt;ud define jenni</strong>: will collect all <strong>define</strong> commands. <br>
     * Note: This will return an empty list if there are no commands matching this request.
     *
     * @param event The MessageEvent which contains the module and commands.
     * @param handler The module which the MessageEvent references.
     * @return A collection of commands relevent to this event.
     */

    public static Collection<Method> getCommands(MessageEvent event, CommandHandler handler) {
        Set<Method> commands = new HashSet<>();
        Collection<Method> methods = getCommandMethods(handler);
        String command = event.getCommand();
        CommandGroup group = null;

        for (Method method : methods) {
            Command annotation = method.getAnnotation(Command.class);

            if (annotation != null) {
                if (Arrays.asList(annotation.aliases()).contains(command)) {
                    group = method.getAnnotation(CommandGroup.class);
                    commands.add(method);
                    break;
                }
            }
        }

        if (commands.isEmpty()) {
            String cString = event.getCommand();

            if (cString != null)
                event.getParams().add(0, cString);

            for (Method method : methods) {
                Default def = method.getAnnotation(Default.class);

                if (def != null) {
                    group = method.getAnnotation(CommandGroup.class);
                    commands.add(method);
                    break;
                }
            }
        }

        if (group != null) {
            for (Method method : methods) {
                CommandGroup annotation = method.getAnnotation(CommandGroup.class);

                if (annotation != null) {
                    if (annotation.value().equals(group.value()))
                        commands.add(method);
                }
            }
        }

        return commands;
    }

    /**
     * Goes through the {@link CommandHandler} and retrieves all
     * methods with the {@link Command} or {@link CommandGroup} annotation.
     *
     * @param handler The handler due the commands.
     * @return A collection of methods this handler has which execute commands.
     */

    public static Collection<Method> getCommandMethods(CommandHandler handler) {
        Collection<Method> methods = new ArrayList<>();
        Method[] array = handler.getClass().getMethods();

        for (Method method : array) {
            Command command = method.getAnnotation(Command.class);
            CommandGroup group = method.getAnnotation(CommandGroup.class);

            if (command != null || group != null)
                methods.add(method);
        }

        return methods;
    }

    /**
     * Filter a set of commands methods by the number of arguments that they have
     * compared to the parameters provided in the MessageEvent. <br>
     * Example: <strong>&gt;ud define jenni false</strong><br>
     * We want the commands method with only two arguments to accomodate this commands.
     * Note: Will return null if no commands matches the same number of arguments.
     *
     * @param event The relevent event containing the module, commands and parameters.
     * @param methods A list of commands events.
     * @return The method required to execute this commands.
     */

    public static Method getByParamCount(MessageEvent event, Collection<Method> methods) {
        methods = methods.stream().filter(o -> {
            long i = Arrays.stream(o.getParameterTypes()).filter(ob -> ob != MessageEvent.class).count();

            return i == event.getParams().size();
        }).collect(Collectors.toList());

        int size = methods.size();

        return size > 0 ? methods.iterator().next() : null;
    }
}
