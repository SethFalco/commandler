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

package org.elypia.commandler.metadata;

import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class MetaParam extends MetaComponent {

    /** Parameter index in the method it is defined in. */
    private int methodIndex;

    /** Param index in the command that it is defined in. */
    private int commandIndex;

    private Parameter parameter;

    /** The default value if null null. */
    private String defaultValue;

    /** The display name for the default value. Null if the default value is viable for display. */
    private String defaultValueDisplay;

    /** If this parameter is required when executing the command. */
    private boolean isOptional;

    /** If this is a single parameter, or a list parameter. */
    private boolean isList;

    public MetaParam(int methodIndex, int commandIndex, Parameter parameter, String name, String description, String defaultValue, String defaultValueDisplay, Properties properties) {
        this.methodIndex = methodIndex;
        this.commandIndex = commandIndex;
        this.parameter = Objects.requireNonNull(parameter);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.defaultValue = defaultValue;
        this.defaultValueDisplay = defaultValueDisplay;
        this.properties = properties;

        isOptional = defaultValue != null;
        isList = parameter.getType().isArray();
    }

    public int getMethodIndex() {
        return methodIndex;
    }

    public int getCommandIndex() {
        return commandIndex;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getDefaultValueDisplay() {
        return defaultValueDisplay;
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
