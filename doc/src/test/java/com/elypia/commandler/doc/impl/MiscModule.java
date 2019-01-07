package com.elypia.commandler.doc.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;

@Icon("fas fa-toolbox")
@Module(id = "Miscellaneous", aliases = "misc", help = "Miscellaneous module with random commands.")
public class MiscModule extends Handler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public MiscModule(Commandler commandler) {
        super(commandler);
    }

    @Example(command = ">misc letters \"Hello, world!\"", response = "There are 13 characters in that text.")
    @Command(id = "Letter Counter", aliases = "letters", help = "Count the number of letters.")
    @Param(id = "body", help = "The body of text to count from.")
    public void countLetters(String body) {
        // Stub
    }
}
