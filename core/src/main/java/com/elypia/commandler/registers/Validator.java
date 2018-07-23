package com.elypia.commandler.registers;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.annotations.validation.Validation;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.validation.*;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Vaidates annotations associated with commands and parameters to ensure
 * they are within the bounds that is specified relative to the input
 * provided when performing the commands.
 */
public class Validator {

    /**
     * A map defining annotations to what {@link ICommandValidator} they are associated with.
     * Command validation validates the entire command itself, this could entail user permissions
     * to perform the command or other requirement.
     */
    private Map<Class<? extends Annotation>, ICommandValidator> commandValidators;

    /**
     * A map defining annotations to what {@link IParamValidator} they are associated with.
     * Parameter validation validates specific parameters, or every item in a list (array)
     * of parameters, this be limiting the length of a String or make sure certain input
     * can't contain profanity.
     */
    private Map<Class<? extends Annotation>, IParamValidator> paramValidators;

    public Validator() {
        commandValidators = new HashMap<>();
        paramValidators = new HashMap<>();

        // ? Registering default validators
        registerValidator(Limit.class, new LimitValidator());
        registerValidator(Length.class, new LengthValidator());
        registerValidator(Period.class, new PeriodValidator());
        registerValidator(Option.class, new OptionValidator());
    }

    public void registerValidator(Class<? extends Annotation> clazz, ICommandValidator validator) {
        registerValidator(clazz);

        if (commandValidators.put(clazz, validator) != null)
            System.err.printf("The previous %s validator has been overwritten for the new implementation provided.\n", clazz.getName());
    }


    public void registerValidator(Class<? extends Annotation> clazz, IParamValidator validator) {
        registerValidator(clazz);

        if (paramValidators.put(clazz, validator) != null)
            System.err.printf("The previous %s validator has been overwritten for the new implementation provided.\n", clazz.getName());
    }

    private void registerValidator(Class<? extends Annotation> clazz) {
        if (!clazz.isAnnotationPresent(Validation.class)) {
            String message = String.format("Annotation for %s doesn't have the Validators annotation.", clazz.getName());
            throw new IllegalStateException(message);
        }
    }

    public <C, E, M> boolean validateCommand(ICommandEvent<C, E, M> event, MetaCommand<C, E, M> metaCommand) {
        for (Map.Entry<MetaValidator, ICommandValidator> entry : metaCommand.getValidators().entrySet()) {
            MetaValidator metaValidator = entry.getKey();
            ICommandValidator validator = entry.getValue();

            if (!validator.validate(event, metaValidator.getValidator())) {
                return false;
            }
        }

        return true;
    }

    public boolean validateParams(CommandEvent event, MetaCommand metaCommand, Object[] args) {
        List<MetaParam> metaParams = metaCommand.getMetaParams();

        for (int i = 0; i < metaParams.size(); i++) {
            MetaParam metaParam = metaParams.get(i);
            Object arg = args[i];
            Class<?> type = metaParam.getParameter().getType();
            Map<MetaValidator, IParamValidator> validators = metaParam.getValidators();

            for (Map.Entry<MetaValidator, IParamValidator> entry : validators.entrySet()) {
                if (type.isArray()) {
                    String[] array = (String[])arg;

                    for (String in : array) {
                        if (!entry.getValue().validate(event, in, entry.getKey().getValidator(), metaParam))
                            return false;
                    }
                } else {
                    if (!entry.getValue().validate(event, arg, entry.getKey().getValidator(), metaParam))
                        return false;
                }
            }
        }

        return true;
    }

    public ICommandValidator getCommandValidator(Class<? extends Annotation> type) {
        if (commandValidators.containsKey(type))
            return commandValidators.get(type);

        return null;
    }

    public IParamValidator getParamValidator(Class<? extends Annotation> type) {
        if (paramValidators.containsKey(type))
            return paramValidators.get(type);

        return null;
    }

    public Map<Class<? extends Annotation>, IParamValidator> getParamValidators() {
        return paramValidators;
    }

    public Map<Class<? extends Annotation>, ICommandValidator> getCommandValidators() {
        return commandValidators;
    }
}
