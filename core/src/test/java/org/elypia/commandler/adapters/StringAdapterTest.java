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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class StringAdapterTest {

    private static StringAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new StringAdapter();
    }

    @ParameterizedTest
    @ValueSource(strings = {"10000", "Hello, world!"})
    public void assertStrings(String value) {
        assertEquals(value, adapter.adapt(value));
    }
}
