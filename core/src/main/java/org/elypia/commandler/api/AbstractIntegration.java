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

package org.elypia.commandler.api;

import org.elypia.commandler.*;
import org.elypia.commandler.config.CommandlerConfig;
import org.slf4j.*;

/**
 * @param <S>
 * @param <M>
 * @author seth@elypia.org (Syed Shah)
 */
public abstract class AbstractIntegration<S, M> implements Integration<S, M> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractIntegration.class);

    protected Commandler commandler;

    public M process(S source, String content) {
        logger.debug("Recieved `{}` from {}.", content, this.getClass());
        AppContext context = commandler.getAppContext();
        Class<? extends ActionListener> listener = context.getConfig().getTypedConfig(CommandlerConfig.class).getActionListener();
        return context.getInjector().getInstance(listener).onAction(this, source, content);
    }

    public Commandler getCommandler() {
        return commandler;
    }
}
