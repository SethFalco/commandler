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

package org.elypia.commandler.models;

import java.util.List;

public class CommandModel {

    private String name;
    private String description;
    private List<ParamModel> params;
    private List<PropertyModel> properties;

    public CommandModel() {
        // Do nothing.
    }

    public CommandModel(String name, String description, List<ParamModel> params, List<PropertyModel> properties) {
        this.name = name;
        this.description = description;
        this.params = params;
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

    public List<ParamModel> getParams() {
        return params;
    }

    public void setParams(List<ParamModel> params) {
        this.params = params;
    }

    public List<PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyModel> properties) {
        this.properties = properties;
    }
}
