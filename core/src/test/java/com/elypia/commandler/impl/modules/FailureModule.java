package com.elypia.commandler.impl.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.TestHandler;

@Module(id = "Failure", aliases = "failure")
public class FailureModule extends TestHandler {

    @Override
    public boolean test() {
        return false;
    }

    @Command(name = "Failure", aliases = "failure")
    public String failure() {
        return "failure";
    }
}
