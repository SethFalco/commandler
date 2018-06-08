package com.elypia.commandlerbot.modules;

import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.annotations.*;

@Module(name = "Misc. Utilities", aliases = {"util", "utils", "math"}, description = "Math commands and fun stuff.")
public class UtilModule extends CommandHandler {

	@Static
	@Command(name = "Letter Count", aliases = "count", help = "Cound the number of characters sent.")
	@Param(name = "text", help = "The text to count from.")
	public String count(String input) {
		return String.format("There are %,d characters in the input text.", input.length());
	}
}
