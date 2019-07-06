package com.elypia.commandler.test.doc.integrated.impl;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Module(name = "osu!", group = "Gaming", aliases = "osu", help = "osu! is a super cool rhythm game everyone plays!")
public class OsuModule implements Handler {

    @Command(name = "Player Stats", aliases = "get", help = "Get stats on osu! players.")
    public void getPlayers(
        @Param(name = "players", help = "The players usernames you want to retrieve.") @Length(min = 3, max = 15) String username,
        @Param(name = "mode", help = "The mode to use when getting players.", defaultValue = "0") @Min(0) @Max(3) int mode
    ) {
        // Stub
    }
}
