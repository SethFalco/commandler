package com.elypia.commandler.string.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.string.StringHandler;

import javax.validation.constraints.Size;

@Module(name = "Validation", aliases = "valid")
public class ValidationModule extends StringHandler {

    @Command(name = "Length", aliases = "length")
    @Param(name = "one", help = "The string that's prepended to the next.")
    @Param(name = "two", help = "The string that's appended to the previous.")
    public String putTogether(@Size(max = 1) String one, @Size(min = 1, max = 10) String two) {
        return one + two;
    }

    @Command(name = "Limit", aliases = "limit")
    @Param(name = "one", help = "The number added to the next one.")
    @Param(name = "two", help = "The number added to the previous one.")
    public long addTogetherLimited(int one, long two) {
        return one + two;
    }

//    @Command(name = "Option", aliases = "option")
//    @Param(name = "name", help = "Any text of the options: seth or jen.")
//    public String showSelected(@Option({"seth", "jen"}) String name) {
//        return name;
//    }
//
//    @Command(name = "Period", aliases = "period")
//    @Param(name = "time", help = "Any period of time under one day.")
//    public String underADay(@Period(max = 1, unit = TimeUnit.DAYS) Duration time) {
//        return DurationParser.forDisplay(time);
//    }
}
