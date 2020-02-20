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

package org.elypia.commandler.adapters;

import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.config.ControllerConfig;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class MetaControllerAdapter implements Adapter<MetaController> {

    private final ControllerConfig controllerConfig;

    @Inject
    public MetaControllerAdapter(final ControllerConfig controllerConfig) {
        this.controllerConfig = controllerConfig;
    }

    @Override
    public MetaController adapt(String input, Class<? extends MetaController> type, MetaParam data, ActionEvent<?, ?> event) {
        List<MetaController> controllers = controllerConfig.getControllers();

        for (MetaController controller : controllers) {
            if (controller.isPublic() && controller.getName().equalsIgnoreCase(input))
                return controller;
        }

        return null;
    }
}
