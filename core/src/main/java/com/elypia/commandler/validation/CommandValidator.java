package com.elypia.commandler.validation;

import com.elypia.commandler.Handler;
import com.elypia.commandler.exceptions.misuse.ParamViolationException;
import com.elypia.commandler.interfaces.CommandlerEvent;
import com.elypia.commandler.metadata.Context;
import com.elypia.commandler.metadata.data.MetaCommand;
import com.google.inject.Injector;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.slf4j.*;

import javax.validation.*;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Vaidates annotations associated with commands and parameters to ensure
 * they are within the bounds that is specified relative to the input
 * provided when performing the commands.
 */
public class CommandValidator {

    /** We use SLF4J to log, be sure to include a binding when using this API at runtime! */
    private static final Logger logger = LoggerFactory.getLogger(CommandValidator.class);

    private final ExecutableValidator exValidator;

    public CommandValidator(Injector injector, Context context) {
        var locator = new PlatformResourceBundleLocator(ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES, null, true);
        ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ResourceBundleMessageInterpolator(locator))
            .constraintValidatorFactory(new InjectableConstraintValidatorFactory(injector))
            .parameterNameProvider(new CommandParamNameProvider(context))
            .buildValidatorFactory();

        Validator validator = factory.getValidator();
        exValidator = validator.forExecutables();
    }

    public <E, M> boolean validate(CommandlerEvent<E, M> event, Handler handler, Object[] parameters) throws ParamViolationException {
        MetaCommand command = event.getInput().getCommand();
        Method method = command.getMethod();
        Set<ConstraintViolation<Handler>> violations = exValidator.validateParameters(handler, method, parameters);

        if (violations.isEmpty())
            return true;

        throw new ParamViolationException(event.getInput(), violations);
    }
}
