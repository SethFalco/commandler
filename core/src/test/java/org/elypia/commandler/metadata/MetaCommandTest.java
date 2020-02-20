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

package org.elypia.commandler.metadata;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MetaCommandTest {

    /** Random method in order to avoid a null pointer exception in the test case. */
    private static final Method METHOD = MetaCommandTest.class.getMethods()[0];

    @Test
    public void testToParamString() {
        MetaParam paramWords = mock(MetaParam.class);
        when(paramWords.getName()).thenReturn("words");
        when(paramWords.isList()).thenReturn(true);
        when(paramWords.isOptional()).thenReturn(false);

        MetaParam paramRandom = mock(MetaParam.class);
        when(paramRandom.getName()).thenReturn("random");
        when(paramRandom.isList()).thenReturn(false);
        when(paramRandom.isOptional()).thenReturn(true);

        String name = "Test Command";
        String help = "This is for testing purposes.";
        Properties props = new Properties();
        List<MetaParam> params = List.of(paramWords, paramRandom);

        MetaCommand command = new MetaCommand(
            METHOD, name, help, false, false, false, props, params
        );

        String expected = "(2) [words] ?random";
        String actual = command.toParamString();

        assertEquals(expected, actual);
    }
}
