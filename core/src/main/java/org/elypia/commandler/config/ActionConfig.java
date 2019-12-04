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

package org.elypia.commandler.config;

import org.elypia.commandler.ActionHandler;
import org.elypia.commandler.api.ActionListener;
import org.elypia.commandler.utils.ReflectionUtils;
import org.slf4j.*;

import javax.inject.*;

/**
 * Configuration for which {@link ActionListener} implementation
 * to use at runtime.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class ActionConfig {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(ActionConfig.class);

    /** A collection of dispatchers that will be created when this Commandler context is run.  */
    private Class<? extends ActionListener> listenerType;

    @Inject
    public ActionConfig(final ConfigService configService) {
        String name = configService.getString("commandler.action.listener");

        if (name != null)
            listenerType = ReflectionUtils.convertType(name, ActionListener.class);
        else
            listenerType = ActionHandler.class;
    }

    public Class<? extends ActionListener> getListenerType() {
        return listenerType;
    }
}
