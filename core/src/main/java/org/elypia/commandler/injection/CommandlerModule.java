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

package org.elypia.commandler.injection;

import com.google.inject.AbstractModule;
import org.elypia.commandler.*;
import org.elypia.commandler.config.ConfigService;

import java.text.NumberFormat;
import java.util.Objects;

/**
 * @author seth@elypia.org (Syed Shah)
 */
public class CommandlerModule extends AbstractModule {

    private final Commandler commandler;
    private final AppContext context;

    public CommandlerModule(final Commandler commandler, final AppContext context) {
        this.commandler = Objects.requireNonNull(commandler);
        this.context = Objects.requireNonNull(context);
    }

    @Override
    protected void configure() {
        bind(Commandler.class).toInstance(commandler);
        bind(AppContext.class).toInstance(context);
        bind(InjectorService.class).toInstance(context.getInjector());
        bind(ConfigService.class).toInstance(context.getConfig());
        bind(NumberFormat.class).toInstance(NumberFormat.getInstance());
    }
}
