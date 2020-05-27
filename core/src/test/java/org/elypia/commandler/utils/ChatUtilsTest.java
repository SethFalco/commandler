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

package org.elypia.commandler.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatUtilsTest {

    @Test
    public void truncate() {
        String expected = "Hello";
        String actual = ChatUtils.truncateAndAppend("Hello, world!", 5);
        assertEquals(expected, actual);
    }

    @Test
    public void truncateWithElipsis() {
        String expected = "Hello...";
        String actual = ChatUtils.truncateAndAppend("Hello, world!", 8, "...");
        assertEquals(expected, actual);
    }

    @Test
    public void testNoTruncatingNeeded() {
        String expected = "Hello, world!";
        String actual = ChatUtils.truncateAndAppend("Hello, world!", Integer.MAX_VALUE, "...");
        assertEquals(expected, actual);
    }
}
