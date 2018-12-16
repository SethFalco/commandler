package com.elypia.commandler.test.impl;

import com.elypia.commandler.*;

public class TestApp {

    private Commandler<String, String> commandler;

    public TestApp() {
        var builder = new Commandler.Builder<String, String>()
            .setPrefix(">");

        commandler = builder.build();
    }

    public String execute(String text) {
        return commandler.execute(null, text, false);
    }

    @SafeVarargs
    final public void add(Class<? extends Handler<String, String>>... clazz) {
        commandler.getContext().addModules(clazz);
    }

    public Commandler<String, String> getCommandler() {
        return commandler;
    }
}
