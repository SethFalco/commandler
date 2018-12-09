package com.elypia.commandler.doc.impl;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Module(id = "osu!", group = "Gaming", aliases = "osu", help = "osu! is a super cool rhythem game everyone plays!")
public class OsuModule extends Handler {

    @Command(id = "Player Stats", aliases = "get", help = "Get stats on osu! players.")
    @Param(id = "players", help = "The players usernames you want to retrieve.")
    public void getPlayers(@Length(min = 3, max = 15) String username) {
        // Stub
    }

    @Overload("Player Stats")
    @Param(id = "mode", help = "The mode to use when getting players.")
    public void getPlayers(@Length(min = 3, max = 15) String username, @Min(0) @Max(3) int mode) {
        // Stub
    }
}
