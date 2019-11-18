/*
 * Copyright 2019-2019 Elypia CIC
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

package org.elypia.commandler.validation;

import org.elypia.commandler.AppContext;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.injection.InjectorService;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.slf4j.*;

import javax.inject.*;
import javax.validation.*;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Vaidates annotations associated with commands and parameters to ensure
 * they are within the bounds that is specified relative to the input
 * provided when performing the commands.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class HibernateValidationManager {

    /** We use SLF4J to log, be sure to include a binding when using this API at runtime! */
    private static final Logger logger = LoggerFactory.getLogger(HibernateValidationManager.class);

    private static final String USER_VALIDATION_MESSAGES = ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES;

    /** The actual validator object constructed and on use throughout Commandler. */
    private final ExecutableValidator exValidator;

    @Inject
    public HibernateValidationManager(InjectorService injectorService, AppContext appContext) {
        var locator = new PlatformResourceBundleLocator(USER_VALIDATION_MESSAGES, null, true);

        ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ResourceBundleMessageInterpolator(locator))
            .constraintValidatorFactory(new InjectableConstraintValidatorFactory(injectorService))
            .parameterNameProvider(new CommandParamNameProvider(appContext))
            .buildValidatorFactory();

        exValidator = factory.getValidator().forExecutables();
    }

    public <S> void validate(ActionEvent<S, ?> event, Controller controller, Object[] parameters) throws RuntimeException {
        Method method = event.getMetaCommand().getMethod();
        logger.debug("Validating {} with parameters {} parameters.", event.getMetaCommand(), parameters.length);
        Set<ConstraintViolation<Controller>> violations = exValidator.validateParameters(controller, method, parameters);

        if (!violations.isEmpty())
            throw new ViolationException(event, violations);
    }
}
