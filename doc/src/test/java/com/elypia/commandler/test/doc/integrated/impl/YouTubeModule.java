package com.elypia.commandler.test.doc.integrated.impl;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;

@Module(name = "YouTube", group = "Media", aliases = {"youtube", "yt"}, help = "Search for videos on YouTube.")
public class YouTubeModule implements Handler {

    @Command(name = "Search", aliases = "search", help = "Search for a video, playlist or channel on YouTube.")

    public void getVideo(
        @Param(name = "query", help = "The search query to look up.") String query
    ) {
        // Stub
    }
}
