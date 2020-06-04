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

import java.util.*;

/**
 * A model object that represents all groups.
 * This should be used to build a localized and display
 * friendly version of all modules.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public class AllGroupsModel {

    /** The model of the controller that represents the {@link ControllerModel} for help. */
    private ControllerModel helpModel;

    /** A map of all groups againt a list of controller models in the group. */
    private Map<String, List<ControllerModel>> groups;

    public AllGroupsModel() {
        // Do nothing.
    }

    public AllGroupsModel(ControllerModel helpModel, Map<String, List<ControllerModel>> groups) {
        this.helpModel = Objects.requireNonNull(helpModel);
        this.groups = Objects.requireNonNull(groups);
    }

    public ControllerModel getHelpModel() {
        return helpModel;
    }

    public void setHelpModel(ControllerModel helpModel) {
        this.helpModel = helpModel;
    }

    public Map<String, List<ControllerModel>> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, List<ControllerModel>> groups) {
        this.groups = groups;
    }
}
