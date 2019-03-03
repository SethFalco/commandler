package com.elypia.commandler;

import com.elypia.commandler.interfaces.CommandlerEvent;
import com.elypia.commandler.metadata.data.CommandData;
import com.elypia.commandler.validation.*;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
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

    private ExecutableValidator exValidator;

    public CommandlerEvent event;

    public CommandValidator(Commandler commandler) {
        this.commandler = commandler;

        ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator(ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES, null, true)))
            .constraintValidatorFactory(new CommandlerConstraintValidatorFactory(this))
            .parameterNameProvider(new CommandParamNameProvider(commandler.getContext()))
            .buildValidatorFactory();

        Validator validator = factory.getValidator();
        exValidator = validator.forExecutables();
    }

    public <E, M> boolean validate(CommandlerEvent<E, M> event, Handler<E, M> handler, Object[] parameters) {
        CommandData command = event.getInput().getCommandData();
        Method method = command.getMethod();

        this.event = event;
        var violations = exValidator.validateParameters(handler, method, parameters);

        if (violations.isEmpty())
            return true;

        event.invalidate(commandler.getMisuseHandler().onInvalidated(event, violations));
        return false;
    }
}
