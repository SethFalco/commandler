package com.elypia.commandler.string.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.parsers.EnumParser;
import com.elypia.commandler.string.StringHandler;

import java.util.concurrent.TimeUnit;

/**
 * This module is for making sure {@link Commandler} is checking
 * enums correctly using {@link EnumParser} but also
 * swapping to use the new implementation rather than the
 * generic one if added.
 */

@Module(name = "Enum", aliases = "enum", help = "The enum module!")
public class EnumModule extends StringHandler {

    @Command(name = "TimeUnit", aliases = "time", help = "I'll tell you if it's a valid unit of time!")
    @Param(name = "unit", help = "A unit of time!")
    public String say(TimeUnit unit) {
        return unit.name();
    }
}
