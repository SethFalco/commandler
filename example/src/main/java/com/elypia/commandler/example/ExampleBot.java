package com.elypia.commandler.example;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.metadata.ContextLoader;

public class ExampleBot {

    public static void main(String[] args) {
        ContextLoader loader = new ContextLoader(ExampleModule.class);
        Commandler commandler = new Commandler(loader);
    }
}
