package com.elypia.commandler.test.integration.impl.modules;

import com.elypia.commandler.adapters.EnumAdapter;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;

import java.util.concurrent.TimeUnit;

/**
 * This module is for making sure {@link Commandler} is checking
 * enums correctly using {@link EnumAdapter} but also
 * swapping to use the new implementation rather than the
 * generic one if added.
 */
@Module(name = "Enum", aliases = "enum", help = "Does Commandler interact with enums correctly?")
public class EnumModule implements Handler {

    enum YouTuber {
        PEWDIEPIE,
        T_SERIES
    }

    @Command(name = "TimeUnit", aliases = "timeunit", help = "Can we adapt the input into an enum with no annotations?")
    public String say(
        @Param(name = "unit", help = "A unit of measuring time.") TimeUnit unit
    ) {
        return unit.name();
    }

    @Command(name = "Top YouTuber", aliases = "top", help = "Can we adapt non standard-library enum with no annotations?")
    public String topYouTuber(
        @Param(name = "youtuber", help = "The number one YouTuber.") YouTuber youtuber
    ) {
        return (youtuber == YouTuber.PEWDIEPIE) ? "K" : "Wrong!";
    }
}
