package com.elypia.commandler.export.test;

import com.elypia.commandler.ModulesContext;
import com.elypia.commandler.export.DocExporter;
import com.elypia.commandler.export.impl.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocExporterTest {

    private static ModulesContext context;
    private static DocExporter exporter;

    @BeforeAll
    public static void beforeAll() {
        context = new ModulesContext();
        exporter = new DocExporter().setContext(context);
    }

    @Test
    public void exportTest() {
        context.addModules(MiscModule.class, OsuModule.class, RuneScapeModule.class, YouTubeModule.class);

        String expected = "{\"modules\":[{\"id\":\"Miscellaneous\",\"group\":\"Miscellaneous\",\"aliases\":[\"misc\"],\"help\":\"Miscellaneous module with random commands.\",\"commands\":[{\"id\":\"Letter Counter\",\"aliases\":[\"letters\"],\"help\":\"Count the number of letters.\",\"parameters\":[{\"id\":\"body\",\"help\":\"The body of text to count from.\"}]}]},{\"id\":\"osu!\",\"group\":\"Gaming\",\"aliases\":[\"osu\"],\"help\":\"osu! is a super cool rhythm game everyone plays!\",\"commands\":[{\"id\":\"Player Stats\",\"aliases\":[\"get\"],\"help\":\"Get stats of osu! players.\",\"parameters\":[{\"id\":\"players\",\"help\":\"The players usernames you want to retrieve.\"}]}]},{\"id\":\"RuneScape\",\"group\":\"Gaming\",\"aliases\":[\"runescape\",\"rs\"],\"help\":\"RuneScape, the popular MMORPG!\",\"commands\":[{\"id\":\"Completed Quests\",\"aliases\":[\"quests\"],\"help\":\"View all quests a player has completed.\",\"parameters\":[{\"id\":\"username\",\"help\":\"The players in-game name.\"}]},{\"id\":\"Player Stats\",\"aliases\":[\"stats\"],\"help\":\"View a players stats.\",\"parameters\":[{\"id\":\"username\",\"help\":\"The players in-game name.\"}]},{\"id\":\"Status\",\"aliases\":[\"status\"],\"help\":\"See an overall status of RuneScape.\",\"parameters\":[]}]},{\"id\":\"YouTube\",\"group\":\"Media\",\"aliases\":[\"youtube\",\"yt\"],\"help\":\"Search for videos on YouTube.\",\"commands\":[{\"id\":\"Search\",\"aliases\":[\"search\"],\"help\":\"Search for a video, playlist or channel on YouTube.\",\"parameters\":[{\"id\":\"query\",\"help\":\"The search query to look up.\"}]}]}],\"groups\":[{\"name\":\"Gaming\",\"modules\":[\"osu!\",\"RuneScape\"]},{\"name\":\"Media\",\"modules\":[\"YouTube\"]},{\"name\":\"Miscellaneous\",\"modules\":[\"Miscellaneous\"]}]}";
        String actual = exporter.toJson();

        assertEquals(expected, actual);
    }
}
