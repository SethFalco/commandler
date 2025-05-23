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

import java.util.Iterator;
import java.util.List;

public class GroupModel implements Iterable<ControllerModel> {

    private String name;
    private List<ControllerModel> controllers;

    public GroupModel() {
        // Do nothing.
    }

    public GroupModel(String name, List<ControllerModel> controllers) {
        this.name = name;
        this.controllers = controllers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ControllerModel> getControllers() {
        return controllers;
    }

    public void setControllers(List<ControllerModel> controllers) {
        this.controllers = controllers;
    }

    @Override
    public Iterator<ControllerModel> iterator() {
        return controllers.iterator();
    }
}
