package com.elypia.commandler.test.doc.integrated.impl;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;

import javax.validation.constraints.Size;

@Module(name = "RuneScape", group = "Gaming", aliases = {"runescape", "rs"}, help = "RuneScape, the popular MMORPG!")
public class RuneScapeModule implements Handler {

    @Command(name = "Status", aliases = "status", help = "See an overall status of RuneScape.")
    public void displayStatus() {
        // Stub
    }

    @Command(name = "Player Stats", aliases = "stats", help = "View a players stats.")
    public void getPlayerStats(
        @Param(name = "username", help = "The players in-game name.") @Size(min = 1, max = 12) String username
    ) {
        // Stub
    }

    @Command(name = "Completed Quests", aliases = "quests", help = "View all quests a player has completed.")

    public void getCompletedQuests(
        @Param(name = "username", help = "The players in-game name.") @Size(min = 1, max = 12) String username
    ) {
        // Stub
    }
}
