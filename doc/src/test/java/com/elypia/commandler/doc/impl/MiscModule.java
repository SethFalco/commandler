package com.elypia.commandler.doc.impl;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.doc.annotations.*;

@Icon("fas fa-toolbox")
@Module(id = "Miscellaneous", aliases = "misc", help = "Miscellaneous module with random commands.")
public class MiscModule extends Handler {

    @Example(command = ">misc letters \"Hello, world!\"", response = "There are 13 characters in that text.")
    @Command(id = "Letter Counter", aliases = "letters", help = "Count the number of letters.")
    @Param(id = "body", help = "The body of text to count from.")
    public void countLetters(String body) {
        // Stub
    }
}
