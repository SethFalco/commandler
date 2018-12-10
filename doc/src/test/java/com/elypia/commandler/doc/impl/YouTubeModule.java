package com.elypia.commandler.doc.impl;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.doc.annotations.Icon;

@Icon(icon = "fab fa-youtube", color = "red")
@Module(id = "YouTube", group = "Media", aliases = {"youtube", "yt"}, help = "Search for videos on YouTube.")
public class YouTubeModule extends Handler {

    @Command(id = "Search", aliases = "search", help = "Search for a video, playlist or channel on YouTube.")
    @Param(id = "query", help = "The search query to look up.")
    public void getVideo(String query) {
        // Stub
    }
}
