package com.elypia.commandler.registers;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.validation.*;
import org.slf4j.*;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Vaidates annotations associated with commands and parameters to ensure
 * they are within the bounds that is specified relative to the input
 * provided when performing the commands.
 */
public class Validator {

    /**
     * This may occur whenever a validator is replaced with another validator.
     * Many implmentations may come with their own validation annotations
     * and validator implementations, but they can be replaced by simply registering
     * another validator implementation to the annotation if the original is not suited.
     */
    private static final String REPLACED_VALIDATOR = "The previous validator for data-type {} ({}) has been replaced with {}.";

    /**
     * We use SLF4J to log, be sure to include a binding when using this API at runtime!
     */
    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    private Commandler commandler;

    private IConfiler confiler;

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

    public Validator(Commandler commandler) {
        this.commandler = commandler;
        confiler = commandler.getConfiler();

        commandValidators = new HashMap<>();
        paramValidators = new HashMap<>();

        // ? Registering default validators
        registerValidator(Limit.class, new LimitValidator(LimitValidator.DEFAULT_HELP));
        registerValidator(Length.class, new Length.Validator(Length.Validator.DEFAULT_HELP));
        registerValidator(Period.class, new PeriodValidator(PeriodValidator.DEFAULT_HELP));
        registerValidator(Option.class, new OptionValidator(OptionValidator.DEFAULT_HELP));
    }

    public void registerValidator(Class<? extends Annotation> clazz, ICommandValidator newValidator) {
        ICommandValidator oldValidator = commandValidators.put(clazz, newValidator);

        if (oldValidator != null) {
            String oldName = oldValidator.getClass().getName();
            String newName = newValidator.getClass().getName();

            logger.info(REPLACED_VALIDATOR, clazz.getName(), oldName, newName);
        }
    }


    public void registerValidator(Class<? extends Annotation> clazz, IParamValidator newValidator) {
        IParamValidator oldValidator = paramValidators.put(clazz, newValidator);

        if (oldValidator != null) {
            String oldName = oldValidator.getClass().getName();
            String newName = newValidator.getClass().getName();

            logger.info(REPLACED_VALIDATOR, clazz.getName(), oldName, newName);
        }
    }

    public <C, E, M> boolean validateCommand(ICommandEvent<C, E, M> event, MetaCommand<C, E, M> metaCommand) {
        for (Map.Entry<MetaValidator, ICommandValidator> entry : metaCommand.getValidators().entrySet()) {
            MetaValidator metaValidator = entry.getKey();
            ICommandValidator validator = entry.getValue();

            if (!validator.validate(event, metaValidator.getValidator())) {
                event.invalidate(confiler.getMisuseListener().onCommandInvalidated(event, metaCommand, validator));
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
                MetaValidator metaValidator = entry.getKey();
                IParamValidator validator = entry.getValue();

                if (type.isArray()) {
                    String[] array = (String[])arg;

                    for (String in : array) {
                        if (!validator.validate(event, in, metaValidator.getValidator(), metaParam)) {
                            event.invalidate(confiler.getMisuseListener().onParameterInvalidated(event, metaCommand, validator));
                            return false;
                        }
                    }
                } else {
                    if (!validator.validate(event, arg, metaValidator.getValidator(), metaParam)) {
                        event.invalidate(confiler.getMisuseListener().onParameterInvalidated(event, metaCommand, validator));
                        return false;
                    }
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
        return Collections.unmodifiableMap(paramValidators);
    }

    public Map<Class<? extends Annotation>, ICommandValidator> getCommandValidators() {
        return Collections.unmodifiableMap(commandValidators);
    }
}
