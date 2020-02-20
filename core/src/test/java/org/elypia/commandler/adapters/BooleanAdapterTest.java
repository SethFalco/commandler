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

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class BooleanAdapterTest {

    private static BooleanAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new BooleanAdapter();
    }

    @Test
    public void testTruthy() {
        assertAll("Check if all these return true.",
            () -> assertTrue(adapter.adapt("true")),
            () -> assertTrue(adapter.adapt("1"))
        );
    }

    @Test
    public void testFalsy() {
        assertAll("Check if all these return false.",
            () -> assertFalse(adapter.adapt("false")),
            () -> assertFalse(adapter.adapt("0"))
        );
    }

    @Test
    public void testNull() {
        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("neither")),
            () -> assertNull(adapter.adapt("-1"))
        );
    }
}
