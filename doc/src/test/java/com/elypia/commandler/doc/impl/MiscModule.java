package com.elypia.commandler.doc.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;

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

    @Command(id = "Letter Counter", aliases = "letters", help = "Count the number of letters.")
    public void countLetters(
        @Param(id = "body", help = "The body of text to count from.") String body
    ) {
        // Stub
    }
}
