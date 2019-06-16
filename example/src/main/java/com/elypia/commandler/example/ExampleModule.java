package com.elypia.commandler.example;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;

@Module(help = "Demonstrates examples to showcase to users.")
public class ExampleModule implements Handler {

    @Command(help = "Repeat back the parameter provided.")
    public String sayCommand(@Param(name = "body", help = "What to say.") String body) {
        return body;
    }

    @Command(help = "Return the fixed text, 'Hello, world!'.")
    public String helloCommand() {
        return "Hello, world!";
    }

    @Command(help = "This command should have varient ways to perform it.")
    public String combineCommand(ExampleCombineParams params) {
        return String.join(":", new String[]{
            params.getOne(), params.getTwo(), params.getThree()
        });
    }
}
