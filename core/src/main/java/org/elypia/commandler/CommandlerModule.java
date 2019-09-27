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

package org.elypia.commandler;

import com.google.inject.AbstractModule;

import java.text.NumberFormat;

/**
 * @author seth@elypia.org (Syed Shah)
 */
public class CommandlerModule extends AbstractModule {

    private final Commandler commandler;

    public CommandlerModule(final Commandler commandler) {
        this.commandler = commandler;
    }

    @Override
    protected void configure() {
        bind(Commandler.class).toInstance(commandler);

        // TODO: This shouldn't be configured by Commandler itself.
        bind(NumberFormat.class).toInstance(NumberFormat.getInstance());
    }
}
