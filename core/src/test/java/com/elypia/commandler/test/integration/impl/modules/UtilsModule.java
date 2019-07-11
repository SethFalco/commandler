package com.elypia.commandler.test.integration.impl.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;

import javax.inject.Singleton;

@Singleton
@Module(name = "Utilities", aliases = "utils", help = "A series of utilities.")
public class UtilsModule implements Handler {

    @Static
    @Command(name = "ping!", aliases = "ping", help = "Returns pong!")
    public String ping() {
        return "pong!";
    }

    @Static
    @Command(name = "Print List", aliases = "list", help = "Print a list of items specified.")
    public String list(@Param(name = "list", help = "List of items.", defaultValue = "${['It', 'helps', 'to', 'specify', 'a', 'list!']}") String[] list) {
        return String.join("\n", list);
    }

    /**
     * Print the name of the service provided, otherwise just print the name
     * of the controller that was used for this command if the service is not
     * specified.
     *
     * @param service The service's name to print.
     * @return The services name.
     */
    @Command(name = "Service", aliases = "service", help = "Print the name of the service.")
    public String service(@Param(name = "service", help = "Name of the service.", defaultValue = "${controller.class.simpleName}") String service) {
        return service;
    }
}
