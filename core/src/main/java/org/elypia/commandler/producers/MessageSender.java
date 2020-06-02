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

package org.elypia.commandler.producers;

import org.elypia.commandler.api.Integration;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.managers.MessengerManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class MessageSender {

    protected Integration<Object, Object> integration;
    protected MessengerManager messengerManager;
    protected ActionEvent<Object, Object> event;

    @Inject
    public MessageSender(Integration integration, MessengerManager messengerManager, ActionEvent event) {
        this.integration = integration;
        this.messengerManager = messengerManager;
        this.event = event;
    }

    public <T> void send(T object) {
        Object message = messengerManager.provide(event, object);
        integration.send(event, message);
    }
}
