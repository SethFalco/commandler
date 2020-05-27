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

package org.elypia.commandler.i18n;

import org.apache.deltaspike.core.api.message.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class CommandlerMessageResolver {

    private final MessageContext messageContext;

    @Inject
    public CommandlerMessageResolver(MessageContext messageContext) {
        this.messageContext = messageContext;
    }

    public String getMessage(String key) {
        Message message = messageContext.messageSource("org.elypia.alexis.i18n.CommandlerMessages").message();
        Message template = message.template(key);
        return template.toString();
    }
}
