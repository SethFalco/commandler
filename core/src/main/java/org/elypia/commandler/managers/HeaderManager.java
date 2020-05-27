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

package org.elypia.commandler.managers;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.CommandlerExtension;
import org.elypia.commandler.api.HeaderBinder;
import org.elypia.commandler.event.Request;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Manage {@link HeaderBinder}s and actually add
 * headers to events before command processing.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class HeaderManager {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(HeaderManager.class);

    private CommandlerExtension extension;

    @Inject
    public HeaderManager(CommandlerExtension extension) {
        this.extension = Objects.requireNonNull(extension);
    }

    public <S, M> void bindHeaders(Request<S, M> request) {
        for (Class<? extends HeaderBinder> binderType : extension.getHeaderBinders()) {
            HeaderBinder binder = BeanProvider.getContextualReference(binderType);
            Map<String, String> headersToAdd = binder.bind(request);

            if (headersToAdd == null)
                continue;

            for (Map.Entry<String, String> entry : headersToAdd.entrySet())
                request.setHeader(entry.getKey(), entry.getValue());
        }
    }
}
