package com.elypia.commandler.string.test;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.string.*;
import com.elypia.commandler.string.client.StringClient;
import com.elypia.commandler.string.modules.ArrayModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MalformedModuleTest {

    private StringCommandler commandler;

    @BeforeEach
    public void beforeEach() {
        commandler = new StringCommandler(new StringClient(), ">");
    }

    @Test
    public void noModuleAnnotation() {
        assertThrows(IllegalStateException.class, () -> {
            // ? We can instantiate this anonymously since we don't need @Module
            commandler.registerModule(new StringHandler() {
                @Command(name = "nomodule", aliases = "nomodule", help = "nomodule")
                public String nomodule() {
                    return "nomodule";
                }
            });
        });
    }

    @Test
    public void testTimeUnitInvalid() {
        commandler.registerModule(new ArrayModule());
        assertThrows(IllegalStateException.class, () -> {
            commandler.registerModule(new ArrayModule());
        });
    }
}
