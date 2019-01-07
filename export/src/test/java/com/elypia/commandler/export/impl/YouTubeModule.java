package com.elypia.commandler.export.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;

@Icon(value = "fab fa-youtube", color = "rgb(255, 0, 0)")
@Module(id = "YouTube", group = "Media", aliases = {"youtube", "yt"}, help = "Search for videos on YouTube.")
public class YouTubeModule extends Handler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public YouTubeModule(Commandler commandler) {
        super(commandler);
    }

    @Command(id = "Search", aliases = "search", help = "Search for a video, playlist or channel on YouTube.")
    @Param(id = "query", help = "The search query to look up.")
    public void getVideo(String query) {
        // Stub
    }
}
