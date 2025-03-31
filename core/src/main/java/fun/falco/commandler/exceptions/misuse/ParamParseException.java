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

package fun.falco.commandler.exceptions.misuse;

import java.util.Objects;

import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.metadata.MetaParam;

/**
 * @author seth@falco.fun (Seth Falco)
 */
public class ParamParseException extends ParamException {

    /** The particular <strong>item</strong> that failed to adapt.*/
    private final String item;

    public ParamParseException(ActionEvent<?, ?> action, MetaParam metaParam, String item) {
        super(action, metaParam);
        this.item = Objects.requireNonNull(item);
    }

    public String getItem() {
        return item;
    }
}
