package com.elypia.commandler.test.adapters;

import com.elypia.commandler.adapters.UrlAdapter;
import org.junit.jupiter.api.*;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class UrlAdapterTest {

    private static UrlAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new UrlAdapter();
    }

    @Test
    public void assertUrls() {
        assertAll("Check the same text provided.",
            () -> assertEquals(new URL("https://elypia.com/"), adapter.adapt("https://elypia.com/")),
            () -> assertEquals(new URL("https://ely.gg/"), adapter.adapt("https://ely.gg/"))
        );
    }
}
