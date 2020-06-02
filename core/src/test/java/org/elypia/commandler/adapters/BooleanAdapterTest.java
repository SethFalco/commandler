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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {"true", "1"})
    public void testTruthy(String value) {
        assertTrue(adapter.adapt(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"false", "0"})
    public void testFalsy(String value) {
        assertFalse(adapter.adapt(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"neither", "-1"})
    public void testNull(String value) {
        assertNull(adapter.adapt(value));
    }
}
