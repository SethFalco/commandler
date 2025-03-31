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

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 4.0.2
 */
public class ExportableCommand implements Serializable {

    public static final long serialVersionUID = 400L;

    private Locale locale;
    private String name;
    private String description;
    private List<ExportableParameter> params;
    private List<ExportableProperty> properties;

    public ExportableCommand() {
        // Do nothing.
    }

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

    public List<ExportableParameter> getParams() {
        return params;
    }

    public void setParams(List<ExportableParameter> params) {
        this.params = params;
    }

    public List<ExportableProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ExportableProperty> properties) {
        this.properties = properties;
    }
}
