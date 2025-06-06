/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

package fun.falco.commandler.adapters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author seth@falco.fun (Seth Falco)
 */
public class CharAdapterTest {

    private static CharAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new CharAdapter();
    }

    @Test
    public void assertChars() {
        assertAll("Check if all these return the character equivilent..",
            () -> assertEquals('A', adapter.adapt("A")),
            () -> assertEquals('1', adapter.adapt("1")),
            () -> assertEquals(',', adapter.adapt(","))
        );
    }

    @Test
    public void assert2Digits() {
        assertAll("Check if all these return character from the ASCII table.",
            () -> assertEquals('A', adapter.adapt("65")),
            () -> assertEquals('z', adapter.adapt("122"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"128", "invalid", "can't be done"})
    public void testNull(String value) {
        assertNull(adapter.adapt(value));
    }
}
