package com.elypia.commandler.annotations;

import com.elypia.commandler.annotation.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnnoUtilsTest {

    @Test
    public void isEffectivelyNull() {
        assertTrue(AnnotationUtils.isEffectivelyNull(AnnotationUtils.EFFECTIVELY_NULL));
    }

    @Test
    public void isNotEffectivelyNull() {
        assertFalse(AnnotationUtils.isEffectivelyNull("Hello, world!"));
    }

    @Test
    public void ifEffectivelyNullThenNull() {
        assertNull(AnnotationUtils.ifEffectivelyNull(AnnotationUtils.EFFECTIVELY_NULL));
    }

    @Test
    public void ifEffectivelyNullThenValue() {
        assertEquals("world", AnnotationUtils.ifEffectivelyNull(AnnotationUtils.EFFECTIVELY_NULL, "world"));
    }
}
