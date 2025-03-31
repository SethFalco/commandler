/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fun.falco.commandler.validation;

import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.apache.deltaspike.beanvalidation.impl.CDIAwareConstraintValidatorFactory;
import fun.falco.commandler.CommandlerExtension;
import fun.falco.commandler.event.ActionEvent;
import org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates annotations associated with commands and parameters to ensure they
 * are within the bounds that is specified relative to the input provided when
 * performing the commands.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
public class HibernateValidationManager {

    /** We use SLF4J to log, be sure to include a binding when using this API at runtime! */
    private static final Logger logger = LoggerFactory.getLogger(HibernateValidationManager.class);

    private static final String USER_VALIDATION_MESSAGES = AbstractMessageInterpolator.USER_VALIDATION_MESSAGES;

    /** The actual validator object constructed and on use throughout Commandler. */
    private final ExecutableValidator exValidator;

    @Inject
    public HibernateValidationManager(final CommandlerExtension commandlerExtension) {
        var locator = new PlatformResourceBundleLocator(USER_VALIDATION_MESSAGES, null, true);

        ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .constraintValidatorFactory(new CDIAwareConstraintValidatorFactory())
            .messageInterpolator(new ResourceBundleMessageInterpolator(locator))
            .parameterNameProvider(new CommandParameterNameProvider(commandlerExtension))
            .buildValidatorFactory();

        exValidator = factory.getValidator().forExecutables();
    }

    public void validate(ActionEvent<?, ?> event, Object controller, Object[] parameters) {
        Method method = event.getMetaCommand().getMethod();
        logger.debug("Validating {} with {} parameters.", event.getMetaCommand(), parameters.length);
        Set<ConstraintViolation<Object>> violations = exValidator.validateParameters(controller, method, parameters);

        if (!violations.isEmpty()) {
            throw new ViolationException(event, violations);
        }
    }
}
