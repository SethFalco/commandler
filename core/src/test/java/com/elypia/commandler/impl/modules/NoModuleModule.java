package com.elypia.commandler.impl.modules;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.impl.StringHandler;

public class NoModuleModule extends StringHandler {

    @Command(name = "nomodule", aliases = "nomodule", help = "nomodule")
    public String nomodule() {
        return "nomodule";
    }
}
