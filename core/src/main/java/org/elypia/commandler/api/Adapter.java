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

package org.elypia.commandler.api;

import org.elypia.commandler.Commandler;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

/**
 * Parsers are what allow {@link Commandler} to know how
 * to interpret an object from the text a chat service provides us.
 * Due to chat injector being comprised of just text we need
 * a {@link Adapter} for each data type we're able or want
 * to be compatible with, and this will allow us to use that data type
 * as a parameter in commands.
 *
 * @param <O> The type of data we want to adapt our input as.
 * @author seth@elypia.org (Syed Shah)
 */
public interface Adapter<O> {

    /**
     * This method should adapt our input provides
     * which is a single parameter into the desired data type.
     *
     * @param metaParam The type of object we're trying to load,
     *             this may not be the same as the parameterised type
     *             {@link O} if this is assignable from the type.
     * @param input The input from the user.
     * @return The parsed data-type, or null if we're unable to adapt the input.
     */
    O adapt(String input, Class<? extends O> type, MetaParam metaParam, ActionEvent<?, ?> event);

    default O adapt(String input) {
        return adapt(input, null);
    }

    default O adapt(String input, Class<? extends O> type) {
        return adapt(input, type, null);
    }

    default O adapt(String input, Class<? extends O> type, MetaParam metaParam) {
        return adapt(input, type, metaParam, null);
    }
}
