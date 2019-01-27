package com.elypia.commandler.test.impl.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.commandler.parsers.EnumParser;

import java.util.concurrent.TimeUnit;

/**
 * This module is for making sure {@link Commandler} is checking
 * enums correctly using {@link EnumParser} but also
 * swapping to use the new implementation rather than the
 * generic one if added.
 */
@Module(id = "Enum", aliases = "enum", help = "Does Commandler interact with enums correctly?")
public class EnumModule extends Handler<String, String> {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public EnumModule(Commandler<String, String> commandler) {
        super(commandler);
    }

    enum YouTuber {
        PEWDIEPIE,
        T_SERIES
    }

    @Command(id = "TimeUnit", aliases = "timeunit", help = "Can we parse the input into an enum with no annotations?")
    public String say(
        @Param(id = "unit", help = "A unit of measuring time.") TimeUnit unit
    ) {
        return unit.name();
    }

    @Command(id = "Top YouTuber", aliases = "top", help = "Can we parse non standard-library enum with no annotations?")
    public String topYouTuber(
        @Param(id = "youtuber", help = "The number one YouTuber.") YouTuber youtuber
    ) {
        return (youtuber == YouTuber.PEWDIEPIE) ? "K" : "Wrong!";
    }
}
