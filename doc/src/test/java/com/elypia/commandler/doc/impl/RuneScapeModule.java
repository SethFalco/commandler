package com.elypia.commandler.doc.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;

import javax.validation.constraints.Size;

@Icon(value = "fas fa-gamepad", color = "#E3EBEA")
@Module(id = "RuneScape", group = "Gaming", aliases = {"runescape", "rs"}, help = "RuneScape, the popular MMORPG!")
public class RuneScapeModule extends Handler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public RuneScapeModule(Commandler commandler) {
        super(commandler);
    }

    @Command(id = "Status", aliases = "status", help = "See an overall status of RuneScape.")
    public void displayStatus() {
        // Stub
    }

    @Command(id = "Player Stats", aliases = "stats", help = "View a players stats.")
    @Param(id = "username", help = "The players in-game name.")
    public void getPlayerStats(@Size(min = 1, max = 12) String username) {
        // Stub
    }

    @Command(id = "Completed Quests", aliases = "quests", help = "View all quests a player has completed.")
    @Param(id = "username", help = "The players in-game name.")
    public void getCompletedQuests(@Size(min = 1, max = 12) String username) {
        // Stub
    }
}
