package com.elypia.commandler.test;

import com.elypia.commandler.exceptions.MalformedModuleException;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.loader.AnnotationLoader;
import com.elypia.commandler.test.impl.broken.MalformedModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MalformedModuleTest {

    @Test
    public void noModuleAnnotation() {
        ContextLoader loader = new ContextLoader(new AnnotationLoader());
        loader.add(MalformedModule.class);

        assertThrows(MalformedModuleException.class, loader::load);
    }
}
