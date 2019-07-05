package com.elypia.commandler.test.doc.integrated;

import com.elypia.commandler.Context;
import com.elypia.commandler.doc.CommandlerDoc;
import com.elypia.commandler.loader.AnnotationLoader;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.test.doc.integrated.impl.*;
import org.junit.jupiter.api.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

public class CommandlerDocTest {

    @Test
    public void docTest() {
        AnnotationLoader loader = new AnnotationLoader(
            MiscModule.class,
            OsuModule.class,
            RuneScapeModule.class,
            YouTubeModule.class
        );

        Context context = new ContextLoader(loader).load().build();
        CommandlerDoc doc = new CommandlerDoc(context.getModules());

        assertNotNull(doc.toJson());
    }
}
