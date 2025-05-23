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

package fun.falco.commandler.models;

import java.util.List;

public class ControllerModel {

    private String name;
    private String description;
    private String group;
    private List<CommandModel> commands;
    private List<PropertyModel> properties;

    public ControllerModel() {
        // Do nothing.
    }

    public ControllerModel(String name, String description, String group, List<CommandModel> commands, List<PropertyModel> properties) {
        this.name = name;
        this.description = description;
        this.group = group;
        this.commands = commands;
        this.properties = properties;
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

    public List<CommandModel> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandModel> commands) {
        this.commands = commands;
    }

    public List<PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyModel> properties) {
        this.properties = properties;
    }
}
