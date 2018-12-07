package com.elypia.commandler.impl.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.TestHandler;
import com.elypia.commandler.parsers.EnumParser;

import java.util.concurrent.TimeUnit;

/**
 * This module is for making sure {@link Commandler} is checking
 * enums correctly using {@link EnumParser} but also
 * swapping to use the new implementation rather than the
 * generic one if added.
 */
@Module(name = "Enum", aliases = "enum", help = "Does Commandler interact with enums correctly?")
public class EnumModule extends TestHandler {

    @Command(name = "TimeUnit", aliases = "timeunit", help = "Can we validate against an enum with no annotations?")
    @Param(name = "unit", help = "A unit of measuring time.")
    public String say(TimeUnit unit) {
        return unit.name();
    }
}
