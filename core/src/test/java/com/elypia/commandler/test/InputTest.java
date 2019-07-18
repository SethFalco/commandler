package com.elypia.commandler.test;

import com.elypia.commandler.Input;
import com.elypia.commandler.metadata.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * We're mocking the metamodule and metacommand because
 * that's not the focus of what this is testing.
 */
public class InputTest {

    /** Mock module for building strings. */
    private MetaModule metaModule;

    /** Mock command for building strings. */
    private MetaCommand metaCommand;

    @BeforeEach
    public void beforeEach() {
        metaModule = mock(MetaModule.class);
        when(metaModule.getName()).thenReturn("RuneScape");

        metaCommand = mock(MetaCommand.class);
        when(metaCommand.getName()).thenReturn("Leaderboard");
    }

    @Test
    public void testGetContent() {
        List<List<String>> params = List.of();
        Input input = new Input(">rs leaderboard", metaModule, metaCommand, params);

        String expected = ">rs leaderboard";
        String actual = input.getContent();

        assertEquals(expected, actual);
    }

    @Test
    public void testToParamString() {
        List<List<String>> params = List.of();
        Input input = new Input(">rs leaderboard", metaModule, metaCommand, params);

        assertAll("Verify with no parameters that toString methods work.",
            () -> assertEquals("None", input.toParamString()),
            () -> assertEquals("RuneScape > Leaderboard | None", input.toString()),
            () -> assertEquals(0, input.getParamCount())
        );
    }

    @Test
    public void testToParamStringWithParams() {
        List<List<String>> params = List.of(List.of("Hello"), List.of("world"));
        Input input = new Input(">rs leaderboard", metaModule, metaCommand, params);

        assertAll("Verify with single parameters that toString methods work.",
            () -> assertEquals("(1) Hello (2) world", input.toParamString()),
            () -> assertEquals("RuneScape > Leaderboard | (1) Hello (2) world", input.toString()),
            () -> assertEquals(2, input.getParamCount())
        );
    }

    @Test
    public void testToStringWithListParams() {
        List<List<String>> params = List.of(List.of("Hello"), List.of("world"), List.of("Hello", "world"));
        Input input = new Input(">rs leaderboard", metaModule, metaCommand, params);

        assertAll("Verify with single parameters that toString methods work.",
            () -> assertEquals("RuneScape > Leaderboard | (1) Hello (2) world (3) [Hello, world]", input.toString()),
            () -> assertEquals(3, input.getParamCount())
        );
    }
}
