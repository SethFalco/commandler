package com.elypia.commandler.validation;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.annotations.validation.Limit;
import com.elypia.commandler.validation.impl.IParamValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Vaidates annotations associated with command paremeters to ensure
 * they are within the bounds that is specified relative to the user
 * performing the command.
 */

public class Validator {

    private Map<Annotation, IParamValidator> paramValidators;

    public Validator() {
        paramValidators = new HashMap<>();
    }

    public void registerValidator(IParamValidator validator) {
//        paramValidators.add(validator);
    }

    public static void validate(MessageEvent event, Method method, Object[] args) throws IllegalArgumentException, IllegalAccessException {
        MetaCommand command = MetaCommand.of(method);

        validateCommand(event, command);
        validateParameters(command, args);
    }

    private static void validateCommand(MessageEvent event, MetaCommand command) throws IllegalAccessException {
        Annotation[] annotations = command.getMethod().getAnnotations();

        for (Annotation annotation : annotations) {
//            if (annotation instanceof Permissions)
//                validatePermissions(event, (Permissions)annotation);
        }
    }

//    private static void validatePermissions(MessageEvent event, Permissions annotation) throws IllegalAccessException {
//
//    }

    private static void validateParameters(MetaCommand command, Object[] args) throws IllegalArgumentException {
        List<MetaParam> params = command.getParams();

        for (int i = 0; i < params.size(); i++) {
            MetaParam parameter = params.get(i);
            Parameter p = parameter.getParameter();

            if (p.isAnnotationPresent(Limit.class)) {
                long d = (int)args[i + 1];
                Limit limit = p.getAnnotation(Limit.class);
                Param param = parameter.getParams();

//                validateLimit(d, limit, param);
            }
        }
    }
}
