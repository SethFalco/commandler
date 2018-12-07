package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.ICommandEvent;
import com.elypia.commandler.metadata.CommandData;
import org.slf4j.*;

import javax.validation.*;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;

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

    private Commandler commandler;

    private Validator validator;
    private ExecutableValidator exValidator;

    public CommandValidator(Commandler commandler) {
        this.commandler = commandler;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        exValidator = validator.forExecutables();
    }

    public <C, E, M> boolean validate(ICommandEvent<C, E, M> event, Object[] parameters) {
        Handler<C, E, M> handler = event.getHandler();
        CommandData command = event.getInput().getCommandData();
        Method method = command.getMethod();

        var violations = exValidator.validateParameters(handler, method, parameters);

        if (violations.isEmpty())
            return true;

        event.invalidate(commandler.getMisuseListener().onParamInvalidated(event, violations));
        return false;
    }
}
