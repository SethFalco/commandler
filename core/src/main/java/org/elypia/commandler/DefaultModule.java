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
import org.elypia.commandler.managers.*;

/**
 * @author seth@elypia.org (Syed Shah)
 */
public class DefaultModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DispatcherManager.class).to(DispatcherManager.class);
        bind(InjectionManager.class).to(InjectionManager.class);
        bind(AdapterManager.class).to(AdapterManager.class);
        bind(TestManager.class).to(TestManager.class);
        bind(DispatcherManager.class).to(DispatcherManager.class);
        bind(ExceptionManager.class).to(ExceptionManager.class);
        bind(MessengerManager.class).to(MessengerManager.class);
    }
}
