package com.elypia.commandler.example;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.meta.ContextLoader;
import com.elypia.commandler.meta.loaders.AnnotationLoader;

public class ExampleBot {

    public static void main(String[] args) {
        // Defined how to obtain meta.
        AnnotationLoader annoLoader = new AnnotationLoader(ExampleModule.class);

        // Create a context loader providing how we want to load data.
        ContextLoader contextLoader = new ContextLoader(annoLoader);

        // Create the Commandler instance with our context.
        Commandler commandler = new Commandler(contextLoader);
    }
}
