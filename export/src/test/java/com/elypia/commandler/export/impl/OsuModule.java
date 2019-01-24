package com.elypia.commandler.export.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Icon(value = "fas fa-gamepad", color = "#EF6DA7")
@Module(id = "osu!", group = "Gaming", aliases = "osu", help = "osu! is a super cool rhythm game everyone plays!")
public class OsuModule extends Handler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public OsuModule(Commandler commandler) {
        super(commandler);
    }

    @Command(id = "Player Stats", aliases = "get", help = "Get stats of osu! players.")
    public void getPlayers(
        @Param(id = "players", help = "The players usernames you want to retrieve.") @Length(min = 3, max = 15) String username
    ) {
        // Stub
    }

    @Overload("Player Stats")
    public void getPlayers(
         @Length(min = 3, max = 15) String username,
         @Param(id = "mode", help = "The mode to use when getting players.") @Min(0) @Max(3) int mode
    ) {
        // Stub
    }
}
