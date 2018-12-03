package com.elypia.commandler.string.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.string.StringHandler;

@Module(name = "Failure", aliases = "failure")
public class FailureModule extends StringHandler {

    @Override
    public boolean test() {
        return false;
    }

    @Command(name = "Failure", aliases = "failure")
    public String failure() {
        return "failure";
    }
}
