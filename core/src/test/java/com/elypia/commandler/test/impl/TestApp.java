package com.elypia.commandler.test.impl;

import com.elypia.commandler.*;

public class TestApp {

    private Commandler<String, String> commandler;

    public TestApp() {
        commandler = new Commandler.Builder<Commandler.Builder<?, String, String>, String, String>()
            .setPrefix(">")
            .build();

        String builderPackage = "com.elypia.commandler.test.impl.builders";
        commandler.getBuilder().addPackage(builderPackage, ITestBuilder.class);
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

