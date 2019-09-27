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

import org.elypia.commandler.event.Action;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Syed Shah)
 */
public class ActionTest {

    @Test
    public void testGetContent() {
        Action action = new Action(">rs leaderboard", "RuneScape", "Leaderboard");

        String expected = ">rs leaderboard";
        String actual = action.getContent();

        assertEquals(expected, actual);
    }

    @Test
    public void testToParamString() {
        Action action = new Action(">rs leaderboard", "RuneScape", "Leaderboard");

        assertAll("Verify with no parameters that toString methods work.",
            () -> assertEquals("None", action.toParamString()),
            () -> assertEquals("RuneScape > Leaderboard | None", action.toString()),
            () -> assertEquals(0, action.getParams().size())
        );
    }

    @Test
    public void testToParamStringWithParams() {
        List<List<String>> params = List.of(List.of("Hello"), List.of("world"));
        Action action = new Action(0, ">rs leaderboard", "RuneScape", "Leaderboard", params);

        assertAll("Verify with single parameters that toString methods work.",
            () -> assertEquals("(1) Hello (2) world", action.toParamString()),
            () -> assertEquals("RuneScape > Leaderboard | (1) Hello (2) world", action.toString()),
            () -> assertEquals(2, action.getParams().size())
        );
    }

    @Test
    public void testToStringWithListParams() {
        List<List<String>> params = List.of(List.of("Hello"), List.of("world"), List.of("Hello", "world"));
        Action action = new Action(0, ">rs leaderboard", "RuneScape", "Leaderboard", params);

        assertAll("Verify with single parameters that toString methods work.",
            () -> assertEquals("RuneScape > Leaderboard | (1) Hello (2) world (3) [Hello, world]", action.toString()),
            () -> assertEquals(3, action.getParams().size())
        );
    }
}
