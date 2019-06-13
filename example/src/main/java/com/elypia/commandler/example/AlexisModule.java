package com.elypia.commandler.example;

import java.util.logging.Handler;

public class AlexisModule extends Handler {

    public String sayCommand(String input) {
        return input;
    }

    public String helloCommand() {
        return "Hello, world!";
    }
}
