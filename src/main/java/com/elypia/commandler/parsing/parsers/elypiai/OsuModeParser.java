package com.elypia.commandler.parsing.parsers.elypiai;

import com.elypia.commandler.parsing.impl.Parser;
import com.elypia.elypiai.osu.data.OsuMode;

public class OsuModeParser implements Parser<OsuMode> {

    private static final OsuMode[] modes = OsuMode.values();

    @Override
    public OsuMode parse(String input) throws IllegalArgumentException {
        for (OsuMode mode : modes) {
            if (input.equals(String.valueOf(mode.getId())))
                return mode;
        }

        if (input.equalsIgnoreCase("osu"))
            return OsuMode.OSU;

        if (input.equalsIgnoreCase("mania") || input.equalsIgnoreCase("piano"))
            return OsuMode.MANIA;

        if (input.equalsIgnoreCase("taiko") || input.equalsIgnoreCase("drums"))
            return OsuMode.TAIKO;

        if (input.equalsIgnoreCase("catch the beat") || input.equalsIgnoreCase("ctb"))
            return OsuMode.CATCH_THE_BEAT;

        throw new IllegalArgumentException("Parameter `" + input + "` isn't a game mode on osu!");
    }
}
