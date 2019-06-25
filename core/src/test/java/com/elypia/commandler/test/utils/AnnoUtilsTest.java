package com.elypia.commandler.test.utils;

import com.elypia.commandler.utils.AnnoUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnnoUtilsTest {

    @Test
    public void isEffectivelyNull() {
        assertTrue(AnnoUtils.isEffectivelyNull(AnnoUtils.EFFECTIVELY_NULL));
    }

    @Test
    public void isNotEffectivelyNull() {
        assertFalse(AnnoUtils.isEffectivelyNull("Hello, world!"));
    }

    @Test
    public void ifEffectivelyNullThenNull() {
        assertNull(AnnoUtils.ifEffectivelyNull(AnnoUtils.EFFECTIVELY_NULL));
    }

    @Test
    public void ifEffectivelyNullThenValue() {
        assertEquals("world", AnnoUtils.ifEffectivelyNull(AnnoUtils.EFFECTIVELY_NULL, "world"));
    }
}
