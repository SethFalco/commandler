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

package org.elypia.commandler.exceptions.misuse;

import java.util.List;
import java.util.Objects;

import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class ListUnsupportedException extends ParamException {

    /** The particular <strong>item</strong> that failed to adapt.*/
    private final List<String> items;

    public ListUnsupportedException(ActionEvent<?, ?> action, MetaParam metaParam, List<String> items) {
        super(action, metaParam);
        this.items = Objects.requireNonNull(items);
    }

    public List<String> getItems() {
        return items;
    }
}
