package com.elypia.commandler.test.integration;

import com.elypia.commandler.exceptions.init.MalformedModuleException;
import com.elypia.commandler.meta.ContextLoader;
import com.elypia.commandler.meta.loaders.AnnotationLoader;
import com.elypia.commandler.test.integration.impl.broken.MalformedModule;
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
