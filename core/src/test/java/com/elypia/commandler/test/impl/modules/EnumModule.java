package com.elypia.commandler.test.impl.modules;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.parsers.EnumParser;
import com.elypia.commandler.test.impl.builders.DefaultBuilder;

import java.util.concurrent.TimeUnit;

/**
 * This module is for making sure {@link Commandler} is checking
 * enums correctly using {@link EnumParser} but also
 * swapping to use the new implementation rather than the
 * generic one if added.
 */
@Parsers(EnumParser.class)
@Builders(DefaultBuilder.class)
@Module(id = "Enum", aliases = "enum", help = "Does Commandler interact with enums correctly?")
public class EnumModule extends Handler<String, String> {

    enum YouTuber {
        PEWDIEPIE,
        T_SERIES
    }

    @Command(id = "TimeUnit", aliases = "timeunit", help = "Can we parse the input into an enum with no annotations?")
    @Param(id = "unit", help = "A unit of measuring time.")
    public String say(TimeUnit unit) {
        return unit.name();
    }

    @Command(id = "Top YouTuber", aliases = "top", help = "Can we parse non standard-library enum with no annotations?")
    @Param(id = "youtuber", help = "The number one YouTuber.")
    public String topYouTuber(YouTuber youtuber) {
        return (youtuber == YouTuber.PEWDIEPIE) ? "K" : "Wrong!";
    }
}
