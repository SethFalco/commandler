package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.CommandData;
import org.slf4j.*;

import javax.validation.*;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Vaidates annotations associated with commands and parameters to ensure
 * they are within the bounds that is specified relative to the input
 * provided when performing the commands.
 */
public class CommandValidator {

    /**
     * We use SLF4J to log, be sure to include a binding when using this API at runtime!
     */
    private static final Logger logger = LoggerFactory.getLogger(CommandValidator.class);

    private IConfiler confiler;

    private Validator validator;
    private ExecutableValidator exValidator;

    public CommandValidator(Commandler commandler) {
        confiler = commandler.getConfiler();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        exValidator = validator.forExecutables();
    }

    public <C, E, M> boolean validateCommand(ICommandEvent<C, E, M> event, CommandData<C, E, M> commandData) {
        for (Map.Entry<MetaValidator, ICommandValidator> entry : commandData.getValidators().entrySet()) {
            MetaValidator metaValidator = entry.getKey();
            ICommandValidator validator = entry.getValue();

            if (!validator.validate(event, metaValidator.getValidator())) {
                event.invalidate(confiler.getMisuseListener().onCommandInvalidated(event, commandData, validator));
                return false;
            }
        }

        return true;
    }

    public <C, E, M> boolean validateParams(ICommandEvent<C, E, M> event, Object[] parameters) {
        CommandData<C, E, M> command = event.getInput().getCommandData();
        IHandler<C, E, M> handler = command.getHandler();
        Method method = command.getMethod();

        var violations = exValidator.validateParameters(handler, method, parameters);

        if (violations.isEmpty())
            return true;

        event.invalidate(confiler.getMisuseListener().onParamInvalidated(event, violations));
        return false;
    }
}
