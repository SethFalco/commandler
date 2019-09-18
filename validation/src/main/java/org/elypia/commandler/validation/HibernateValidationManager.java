package org.elypia.commandler.validation;

import org.elypia.commandler.Context;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.managers.InjectionManager;
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
public class HibernateValidationManager {

    /** We use SLF4J to log, be sure to include a binding when using this API at runtime! */
    private static final Logger logger = LoggerFactory.getLogger(HibernateValidationManager.class);

    private static final String USER_VALIDATION_MESSAGES = ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES;

    /** The actual validator object constructed and on use throughout Commandler. */
    private final ExecutableValidator exValidator;

    public HibernateValidationManager(InjectionManager injectionManager, Context context) {
        var locator = new PlatformResourceBundleLocator(USER_VALIDATION_MESSAGES, null, true);

        ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ResourceBundleMessageInterpolator(locator))
            .constraintValidatorFactory(new InjectableConstraintValidatorFactory(injectionManager))
            .parameterNameProvider(new CommandParamNameProvider(context))
            .buildValidatorFactory();

        exValidator = factory.getValidator().forExecutables();
    }

    public <S> void validate(ActionEvent<S, ?> event, Controller controller, Object[] parameters) throws RuntimeException {
        Method method = event.getMetaCommand().getMethod();
        Set<ConstraintViolation<Controller>> violations = exValidator.validateParameters(controller, method, parameters);

        if (!violations.isEmpty())
            throw new ViolationException(event, violations);
    }
}
