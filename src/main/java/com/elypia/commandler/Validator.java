package com.elypia.commandler;

import com.elypia.commandler.jda.annotations.access.Permissions;
import com.elypia.commandler.annotations.command.Param;
import com.elypia.commandler.annotations.validation.Limit;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.elypiai.utils.ElyUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * Vaidates annotations associated with command paremeters to ensure
 * they are within the bounds that is specified relative to the user
 * performing the command.
 */

public class Validator {

    public static void validate(MessageEvent event, Method method, Object[] args) throws IllegalArgumentException, IllegalAccessException {
        MetaCommand command = new MetaCommand(method);

        validateCommand(event, command);
        validateParameters(command, args);
    }

    private static void validateCommand(MessageEvent event, MetaCommand command) throws IllegalAccessException {
        Annotation[] annotations = command.getMethod().getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof Developer)
                validateDeveloper(event.getAuthor());

            if (annotation instanceof Permissions)
                validatePermissions(event, (Permissions)annotation);
        }
    }

    private static void validatePermissions(MessageEvent event, Permissions annotation) throws IllegalAccessException {
        Permission[] permissions = annotation.value();

        if (!event.getMember().hasPermission(permissions)) {
            String string = String.join(", ", ElyUtils.toStringArray(permissions));
            String format = "Sorry but the command `%s` in module `%s`, requires the permissions: `%s`.\nYou lack the rights to perform this command here.";
            String message = String.format(format, event.getCommand(), event.getModule(), string);
            throw new IllegalAccessException(message);
        }
    }

    private static void validateParameters(MetaCommand command, Object[] args) throws IllegalArgumentException {
        List<MetaParam> params = command.getParams();

        for (int i = 0; i < params.size(); i++) {
            MetaParam parameter = params.get(i);
            Parameter p = parameter.getParameter();

            if (p.isAnnotationPresent(Limit.class)) {
                long d = (int)args[i + 1];
                Limit limit = p.getAnnotation(Limit.class);
                Param param = parameter.getParams();

                validateLimit(d, limit, param);
            }
        }
    }

    private static void validateDeveloper(User user) throws IllegalAccessException {
        if (!Config.isDeveloper(user)) {
            String format = "Sorry, only the developers of %s are allowed to perform this command.";
            String message = String.format(format, user.getJDA().getSelfUser().getName());
            throw new IllegalAccessException(message);
        }
    }

    private static void validateLimit(double value, Limit limit, Param parameter) throws IllegalArgumentException {
        if (value < limit.min() || value > limit.max()) {
            String format = "Sorry, the paramter `%s` must be between %,d and %,d!\n\nThe documentation states:\n%s";
            String help = "`" + parameter.name() + ": " + parameter.help() + "`";
            String message = String.format(format, parameter.name(), limit.min(), limit.max(), help);
            throw new IllegalArgumentException(message);
        }
    }
}
