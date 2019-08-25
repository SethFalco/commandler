package com.elypia.commandler;

import com.elypia.commandler.event.Action;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
