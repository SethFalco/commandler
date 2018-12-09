package com.elypia.commandler.doc.impl;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;

@Module(id = "Miscellaneous", aliases = "misc", help = "Miscellaneous module with random commands.")
public class MiscModule extends Handler {

    @Command(id = "Letter Counter", aliases = "letters", help = "Count the number of letters.")
    @Param(id = "body", help = "The body of text to count from.")
    public void countLetters(String body) {
        // Stub
    }
}
