package org.elypia.commandler.adapters;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class UrlAdapterTest {

    @Test
    public void testUrls() {
        UrlAdapter adapter = new UrlAdapter();

        assertAll("Check the same text provided.",
            () -> assertEquals(new URL("https://elypia.com/"), adapter.adapt("https://elypia.com/")),
            () -> assertEquals(new URL("https://ely.gg/"), adapter.adapt("https://ely.gg/"))
        );
    }

    @Test
    public void testMalformedUrls() {
        UrlAdapter adapter = new UrlAdapter();
        assertNull(adapter.adapt("I'm an invalid URL!"));
    }
}
