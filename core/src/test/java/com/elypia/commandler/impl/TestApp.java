package com.elypia.commandler.impl;

import com.elypia.commandler.*;

public class TestApp {

    private Commandler<Void, String, String> commandler;

    public TestApp() {
        var builder = new Commandler.Builder<Void, String, String>()
            .setPrefix(">");

        commandler = builder.build();
    }

    public String execute(String text) {
        return commandler.execute(null, text, false);
    }

    @SafeVarargs
    final public void add(Class<? extends Handler<Void, String, String>>... clazz) {
        commandler.getContext().addModule(clazz);
    }
}
