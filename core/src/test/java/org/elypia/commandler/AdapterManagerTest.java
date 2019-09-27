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

import org.elypia.commandler.adapters.*;
import org.elypia.commandler.exceptions.AdapterRequiredException;
import org.elypia.commandler.managers.*;
import org.elypia.commandler.metadata.MetaAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Syed Shah)
 */
public class AdapterManagerTest {

    @Test
    public void adapterTest() {
        MetaAdapter meta = new MetaAdapter(BooleanAdapter.class, Boolean.class, boolean.class);
        AdapterManager adapter = new AdapterManager(new InjectionManager(), meta);

        assertAll("Verify we can get our adapter back correctly.",
            () -> assertNotNull(adapter.getAdapter(Boolean.class)),
            () -> assertThrows(AdapterRequiredException.class, () -> adapter.getAdapter(StringAdapter.class))
        );
    }
}
