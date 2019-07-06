package com.elypia.commandler.test.integration.impl.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.loaders.AnnotationLoader;

import javax.inject.Singleton;

/**
 * This is for testing if Commandler will correctly
 * reject a module that has everything set up correctly
 * except it's missing the {@link Module} annotation.
 * Should only fail if only the {@link AnnotationLoader}
 * is in use.
 */
@Singleton
public class MalformedModule implements Handler {

    @Command(name = "Fail", aliases = "fail", help = "A command that can never complete successfully.")
    public String fail() {
        return "This module should fail to load due to no module annotation.";
    }
}
