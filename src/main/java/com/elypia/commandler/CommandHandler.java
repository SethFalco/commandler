package com.elypia.commandler;

import com.elypia.commandler.annotations.command.*;
import com.elypia.commandler.metadata.*;
import net.dv8tion.jda.core.JDA;

public abstract class CommandHandler {

	protected JDA jda;

	/**
	 * If this module is enabled or out of service.
	 */

	protected boolean enabled;

	public boolean test() {
		return true;
	}

	@Command(aliases = "help", help = "Displays all help information for commands in the module.")
	public String help() {
		MetaModule meta = MetaModule.of(this);
		Module module = meta.getModule();

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("** %s**\n", module.aliases()[0]));
		builder.append(module.help() + "\n");

		for (MetaCommand metaCommand : meta.getCommands()) {
			Command command = metaCommand.getCommand();

			if (!command.help().isEmpty())
				builder.append(String.format("`%s`: %s\n", command.aliases()[0], command.help()));
		}

		return builder.toString();
	}

	public boolean isEnabled() {
		return enabled;
	}

	protected JDA getJDA() {
		return jda;
	}

	public void setJDA(JDA jda) {
		this.jda = jda;
	}
}
