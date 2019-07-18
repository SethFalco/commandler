package com.elypia.commandler.managers;

import com.elypia.commandler.*;
import com.elypia.commandler.exceptions.ParamViolationException;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.validation.*;
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
public class ValidationManager {

    /** We use SLF4J to log, be sure to include a binding when using this API at runtime! */
    private static final Logger logger = LoggerFactory.getLogger(ValidationManager.class);

    /** The actual validator object constructed and on use throughout Commandler. */
    private final ExecutableValidator exValidator;

    public ValidationManager(InjectionManager injectionManager, Context context) {
        var locator = new PlatformResourceBundleLocator(ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES, null, true);
        ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ResourceBundleMessageInterpolator(locator))
            .constraintValidatorFactory(new InjectableConstraintValidatorFactory(injectionManager))
            .parameterNameProvider(new CommandParamNameProvider(context))
            .buildValidatorFactory();

        exValidator = factory.getValidator().forExecutables();
    }

    public <E> void validate(CommandlerEvent<E, ?> event, Handler handler, Object[] parameters) throws ParamViolationException {
        Method method = event.getInput().getCommand().getMethod();
        Set<ConstraintViolation<Handler>> violations = exValidator.validateParameters(handler, method, parameters);

        if (!violations.isEmpty())
            throw new ParamViolationException(event.getInput(), violations);
    }
}
