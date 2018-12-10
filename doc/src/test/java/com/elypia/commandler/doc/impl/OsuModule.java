package com.elypia.commandler.doc.impl;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.doc.annotations.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Icon(icon = "fas fa-gamepad", color = "pink")
@Module(id = "osu!", group = "Gaming", aliases = "osu", help = "osu! is a super cool rhythm game everyone plays!")
public class OsuModule extends Handler {

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
