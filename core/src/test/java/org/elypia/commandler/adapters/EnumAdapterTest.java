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

import org.junit.jupiter.api.*;

import java.time.DayOfWeek;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class EnumAdapterTest {

    private static EnumAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new EnumAdapter();
    }

    @Test
    public void assertEnums() {
        assertAll("Check if all these return the enum constant.",
            () -> assertEquals(DayOfWeek.FRIDAY, adapter.adapt("friday", DayOfWeek.class)),
            () -> assertEquals(TimeUnit.SECONDS, adapter.adapt("seconds", TimeUnit.class))
        );
    }

    @Test
    public void testNull() {
        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("invalid", DayOfWeek.class))
        );
    }
}
