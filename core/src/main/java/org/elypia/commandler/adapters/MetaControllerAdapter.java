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

import org.elypia.commandler.CommandlerExtension;
import org.elypia.commandler.annotation.stereotypes.ParamAdapter;
import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.dispatchers.StandardDispatcher;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.i18n.CommandlerMessageResolver;
import org.elypia.commandler.metadata.*;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ParamAdapter(MetaController.class)
public class MetaControllerAdapter implements Adapter<MetaController> {

    private final CommandlerExtension commmanderExtension;
    private final CommandlerMessageResolver resolver;

    @Inject
    public MetaControllerAdapter(CommandlerExtension commmanderExtension, CommandlerMessageResolver resolver) {
        this.commmanderExtension = commmanderExtension;
        this.resolver = resolver;
    }

    @Override
    public MetaController adapt(String input, Class<? extends MetaController> type, MetaParam data, ActionEvent<?, ?> event) {
        Collection<MetaController> controllers = commmanderExtension.getMetaControllers();

        for (MetaController controller : controllers) {
            if (controller.isHidden())
                continue;

            if (resolver.getMessage(controller.getName()).equalsIgnoreCase(input))
                return controller;

            // TODO: Use activators instead?
            String aliases = resolver.getMessage(controller.getProperty(StandardDispatcher.class, "aliases"));

            if (aliases.equalsIgnoreCase(input))
                return controller;
        }

        return null;
    }
}
