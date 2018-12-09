package com.elypia.commandler.impl;

import com.elypia.commandler.*;

public class TestApp {

    private Commandler<Void, String, String> commandler;
    private ModulesContext context;

    public TestApp() {
        context = new ModulesContext();

        var builder = new Commandler.Builder<Void, String, String>()
            .setContext(context)
            .setPrefix(">");

        commandler = builder.build();
    }

    public String execute(String text) {
        return commandler.execute(null, text, false);
    }

    @SafeVarargs
    final public void add(Class<? extends Handler<Void, String, String>>... clazz) {
        context.addModule(clazz);
    }

    public Commandler<Void, String, String> getCommandler() {
        return commandler;
    }
}
