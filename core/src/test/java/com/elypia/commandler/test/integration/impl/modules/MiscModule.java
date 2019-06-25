package com.elypia.commandler.test.integration.impl.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import org.hibernate.validator.constraints.Length;

import javax.inject.Singleton;
import javax.validation.constraints.Min;

@Singleton
@Module(name = "Miscellaneous", aliases = "misc", help = "Test generic functionality and if it works.")
public class MiscModule implements Handler {

    @Command(name = "Repeat", aliases = {"repeat", "say"}, help = "Repeat text one or more times.")
    public String repeatCommand(
        @Length(min = 1) @Param(name = "input", help = "What to say?") String input,
        @Min(1) @Param(name = "count", help = "How many times?", defaultValue = "1") int count
    ) {
        return input.repeat(count);
    }
}
