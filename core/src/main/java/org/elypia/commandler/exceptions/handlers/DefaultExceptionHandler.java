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

package org.elypia.commandler.exceptions.handlers;

import org.apache.deltaspike.core.api.exception.control.*;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.elypia.commandler.producers.MessageSender;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * The default handling for certain {@link Exception}s
 * that haven't been handled by any other handler.
 *
 * Sends a generic failure message to say something went wrong.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
@ExceptionHandler
public class DefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    private final MessageSender sender;
    private final DefaultExceptionHandlerConfiguration config;

    @Inject
    public DefaultExceptionHandler(MessageSender sender, DefaultExceptionHandlerConfiguration config) {
        this.sender = sender;
        this.config = config;
    }

    /**
     * @param exEvent The exception that occured.
     */
    public void onException(@Handles(ordinal = Integer.MIN_VALUE) ExceptionEvent<Exception> exEvent) {
        if (exEvent.isMarkedHandled())
            return;

        logger.error("An uncaught and unhandled exception occured during the request. Sending default message.", exEvent.getException());
        sender.send(config.getGenericExceptionMessage());
    }
}
