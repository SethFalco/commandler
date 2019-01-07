package com.elypia.commandler.doc.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
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

    @Example(command = ">osu get seth", response =
        "SethX3\n\n" +
        "Level: 98  \n" +
        "Ranked Score: 4,446,893,858  \n" +
        "Total Score: 13,439,260,416  \n" +
        "PP: 2,653.84  \n" +
        "Rank: 74,052  \n" +
        "Accuracy: 94.21%  \n" +
        "Play Count: 21,918\n\n" +
        "https://osu.ppy.sh/u/4185808"
    )
    @Command(id = "Player Stats", aliases = "get", help = "Get stats of osu! players.")
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
