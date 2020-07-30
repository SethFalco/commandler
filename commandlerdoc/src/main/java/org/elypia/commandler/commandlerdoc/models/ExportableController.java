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

package org.elypia.commandler.commandlerdoc.models;

import org.elypia.commandler.metadata.MetaController;

import java.io.Serializable;
import java.util.*;

/**
 * This represents a completely localized and ready to export
 * view of the {@link MetaController}.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 4.0.2
 */
public class ExportableController implements Serializable {

    public static final long serialVersionUID = 400L;

    /** The locale of this exportable controller, this determines the language the contents is in. */
    private Locale locale;

    /** The name of title of this controller. */
    private String name;

    /** The description of this controller. */
    private String description;

    /** The group this controller belongs inside of. */
    private String group;

    /** A list of commands that are in this controller. */
    private List<ExportableCommand> commands;

    /** A list of public properties that are in this controller. */
    private List<ExportableProperty> properties;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ExportableCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<ExportableCommand> commands) {
        this.commands = commands;
    }

    public List<ExportableProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ExportableProperty> properties) {
        this.properties = properties;
    }
}
