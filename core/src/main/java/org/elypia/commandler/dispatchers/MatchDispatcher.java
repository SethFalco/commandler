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

package org.elypia.commandler.dispatchers;

import org.elypia.commandler.api.*;
import org.elypia.commandler.event.ActionEvent;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class MatchDispatcher implements Dispatcher {

    /**
     * Any message could match a potential regular expression.
     * As a result all messages are valid Match commands.
     *
     * @param event The source event that called this command.
     * @param content The content of the command.
     * @return If this is a valid command or not.
     */
    @Override
    public boolean isValid(Object event, String content) {
        return true;
    }

    @Override
    public <S, M> ActionEvent<S, M> parse(Integration<S, M> controller, S source, M message, String content) {
        return null;
    }
}
