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

package fun.falco.commandler.api;

import fun.falco.commandler.event.ActionEvent;

/**
 * This should implement the default load method. The default method should be
 * the standard or minimal way to send messages in a chat channel, usually raw
 * text. Platform specific implementations may append more methods according to
 * the different types of messages.
 *
 * @author seth@falco.fun (Seth Falco)
 * @param <O> Object convert into a message.
 * @param <M> Message we're returning.
 */
public interface Messenger<O, M> {

    /**
     * The default load method, this adapters should be the default way to send
     * an {@link O object} of this type as a message. Platform specific
     * implementations may implement more load methods for the different message
     * formats, if so this load method should return the basic {@link M message}
     * that requires minimal permissions.
     *
     * @param event User caused event which represents the command.
     * @param output Output from the {@link Adapter} when parsing the input.
     * @return Message to response to the user.
     */
    M provide(ActionEvent<?, M> event, O output);
}
