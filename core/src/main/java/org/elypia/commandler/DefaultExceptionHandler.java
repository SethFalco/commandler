/*
 * Copyright 2019-2020 Elypia CIC
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

package org.elypia.commandler;

import org.apache.deltaspike.core.api.exception.control.*;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.elypia.commandler.exceptions.misuse.AbstractMisuseException;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * TODO: Exceptions don't send messages anymroe
 * TODO: Make help command work again
 *
 * The default handling for certain {@link AbstractMisuseException}
 * that occur during runtime.
 *
 * It's recommend when configuring your own exceptions
 * to derive from this class as a base.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
@ExceptionHandler
public class DefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    private final DefaultExceptionHandlerConfiguration config;

    @Inject
    public DefaultExceptionHandler(DefaultExceptionHandlerConfiguration config) {
        this.config = config;
    }

    /**
     * @param ex The exception that occured.
     */
    public void onException(@Handles ExceptionEvent<Exception> ex) {
        logger.error("An uncaught exception occured during command handling.", ex.getException());
        logger.info(config.getGenericExceptionMessage());
    }
}
