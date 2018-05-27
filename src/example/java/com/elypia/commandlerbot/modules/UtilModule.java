package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.Static;

@Module(
	name = "Misc. Utilities",
	aliases = {"util", "utils", "math"},
	description = "Math commands and fun stuff."
)
public class UtilModule extends CommandHandler {

	@Static
	@Command(aliases = "count", help = "Cound the number of characters sent.")
	@Param(name = "text", help = "The text to count from.")
	public String count(String input) {
		return String.format("There are %,d characters in the input text.", input.length());
	}
}
