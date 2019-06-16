package com.elypia.commandler.test.utils;

import com.elypia.commandler.def.DefDispatcher;
import com.elypia.commandler.utils.ReflectionUtils;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionUtilsTest {

    @Test
    public void getClassesFromClass() {
        Collection<Class<?>> types = ReflectionUtils.getClasses(ReflectionUtils.class);
        assertEquals(3, types.size());
    }

    @Test
    public void getClassesFromPackage() {
        Collection<Class<?>> types = ReflectionUtils.getClasses(ReflectionUtils.class.getPackage());
        assertEquals(3, types.size());
    }

    @Test
    public void getClassesRecursively() {
        Collection<Class<?>> types = ReflectionUtils.getClasses(DefDispatcher.class);
        assertEquals(4, types.size());
    }
}
