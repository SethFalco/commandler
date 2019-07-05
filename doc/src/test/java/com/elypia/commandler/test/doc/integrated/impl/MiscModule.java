package com.elypia.commandler.test.doc.integrated.impl;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;

@Module(name = "Miscellaneous", aliases = "misc", help = "Miscellaneous module with random commands.")
public class MiscModule implements Handler {

    @Command(name = "Letter Counter", aliases = "letters", help = "Count the number of letters.")
    public void countLetters(
        @Param(name = "body", help = "The body of text to count from.") String body
    ) {
        // Stub
    }
}
