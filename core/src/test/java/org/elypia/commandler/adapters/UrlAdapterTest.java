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

package org.elypia.commandler.adapters;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
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
