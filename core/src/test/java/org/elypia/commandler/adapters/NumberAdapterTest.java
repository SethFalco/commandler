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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class NumberAdapterTest {

    @Test
    public void assertNumbers() {
        NumberAdapter adapter = new NumberAdapter();

        assertAll("Check if all these return the number equivilent.",
            () -> assertEquals(20, adapter.adapt("20")),
            () -> assertEquals(-500, adapter.adapt("-500")),
            () -> assertEquals(2147, adapter.adapt("2147"))
        );
    }

    @Test
    public void assertItaly() {
        NumberAdapter adapter = new NumberAdapter(NumberFormat.getInstance(Locale.ITALY));

        assertAll("Check if all these return the number equivilent.",
            () -> assertEquals(20000.456, adapter.adapt("20.000,456", Double.class)),
            () -> assertEquals(-1234345, adapter.adapt("-1.234.345"))
        );
    }

    @Test
    public void assertUk() {
        NumberAdapter adapter = new NumberAdapter(NumberFormat.getInstance(Locale.UK));

        assertAll("Check if all these return the number equivilent.",
            () -> assertEquals(20000.456, adapter.adapt("20,000.456", Double.class)),
            () -> assertEquals(-1234345, adapter.adapt("-1,234,345"))
        );
    }

    /** Tests cases where it starts with a number, but non-numeric text after. */
    @Test
    public void assertPartialMatch() {
        NumberAdapter adapter = new NumberAdapter(NumberFormat.getInstance(Locale.UK));

        assertAll("Check if all these return the number equivilent.",
            () -> assertNull(adapter.adapt("20,000.456 no u", Double.class)),
            () -> assertNull(adapter.adapt("-1,234,345pewpew"))
        );
    }

    @Test
    public void testNull() {
        NumberAdapter adapter = new NumberAdapter();

        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("invalid"))
        );
    }

    @Test
    public void testLongs() {
        NumberAdapter adapter = new NumberAdapter();

        Number expected = 2647039049039L;
        Number actual = adapter.adapt("2647039049039", Long.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testShorts() {
        NumberAdapter adapter = new NumberAdapter();

        Number expected = (short)420;
        Number actual = adapter.adapt("420", Short.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testBytes() {
        NumberAdapter adapter = new NumberAdapter();

        Number expected = (byte)3;
        Number actual = adapter.adapt("3", Byte.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testFloats() {
        NumberAdapter adapter = new NumberAdapter(NumberFormat.getInstance(Locale.UK));

        Number expected = 3.1415f;
        Number actual = adapter.adapt("3.1415", Float.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidType() {
        NumberAdapter adapter = new NumberAdapter();
        assertThrows(IllegalStateException.class, () -> adapter.adapt("12345", BigDecimal.class));
    }
}
