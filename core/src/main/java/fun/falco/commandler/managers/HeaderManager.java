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

package fun.falco.commandler.managers;

import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import fun.falco.commandler.CommandlerExtension;
import fun.falco.commandler.api.HeaderBinder;
import fun.falco.commandler.event.Request;

/**
 * Manage {@link HeaderBinder}s and actually add headers to events before
 * command processing.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
public class HeaderManager {

    private CommandlerExtension extension;

    @Inject
    public HeaderManager(CommandlerExtension extension) {
        this.extension = Objects.requireNonNull(extension);
    }

    public <S, M> void bindHeaders(Request<S, M> request) {
        for (Class<? extends HeaderBinder> binderType : extension.getHeaderBinders()) {
            HeaderBinder binder = BeanProvider.getContextualReference(binderType);
            Map<String, String> headersToAdd = binder.bind(request);

            if (headersToAdd == null) {
                continue;
            }

            for (Map.Entry<String, String> entry : headersToAdd.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }
}
