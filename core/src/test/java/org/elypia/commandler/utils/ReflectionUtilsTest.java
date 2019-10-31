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

package org.elypia.commandler.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionUtilsTest {

    @Test
    public void testConvertTypesWithTypesObject() {
        Class<ReflectionUtils> expected = ReflectionUtils.class;
        Class<ReflectionUtils> actual = ReflectionUtils.convertType(
            "org.elypia.commandler.utils.ReflectionUtils", ReflectionUtils.class
        );

        assertEquals(expected, actual);
    }

    @Test
    public void testConvertTypesWithNonTypesObject() {
        Class<ReflectionUtils> expected = ReflectionUtils.class;
        Class<Object> actual = ReflectionUtils.convertType(
            "org.elypia.commandler.utils.ReflectionUtils"
        );

        assertEquals(expected, actual);
    }

    @Test
    public void testConvertTypesNull() {
        assertThrows(NullPointerException.class, () -> ReflectionUtils.convertTypes((String[])null));
    }
}
