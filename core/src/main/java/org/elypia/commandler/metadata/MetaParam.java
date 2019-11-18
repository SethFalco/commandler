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

package org.elypia.commandler.metadata;

import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class MetaParam extends MetaComponent {

    /** Parameter index in the method it is defined in. */
    private int index;

    private Parameter parameter;

    /** The default value if any else null. */
    private String defaultValue;

    /** If this parameter is required when executing the command. */
    private boolean isOptional;

    /** If this is a single parameter, or a list parameter. */
    private boolean isList;

    public MetaParam(int index, Parameter parameter, String name, String description, String defaultValue, Properties properties) {
        this.index = index;
        this.parameter = Objects.requireNonNull(parameter);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.defaultValue = defaultValue;
        this.properties = properties;

        isOptional = defaultValue != null;
        isList = parameter.getType().isArray();
    }

    public int getIndex() {
        return index;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public boolean isRequired() {
        return !isOptional;
    }

    public boolean isList() {
        return isList;
    }
}
