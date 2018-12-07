package com.elypia.commandler.impl;

import com.elypia.commandler.*;

import java.io.IOException;

public class TestApp {

    private Commandler<Void, String, String> commandler;
    private ModulesContext context;

    public TestApp() {
        context = new ModulesContext();

        var builder = new CommandlerBuilder<Void, String, String>()
            .setContext(context)
            .setPrefix("!")
            .setWebsite("https://commandler.elypia.com/");

        commandler = builder.build();
    }

    public String execute(String text) {
        return commandler.trigger(null, text);
    }

    public void addAll() throws IOException {
        context.addPackage("com.elypia.commandler.impl.modules");
    }

    @SafeVarargs
    final public void add(Class<? extends TestHandler>... clazz) {
        context.addModule(clazz);
    }
}
