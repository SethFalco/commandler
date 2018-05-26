package com.elypia.commandler;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.metadata.MetaModule;

public abstract class CommandHandler {

	/**
	 * If this module is enabled or out of service.
	 */

	protected boolean enabled;

	public boolean test() {
		return true;
	}

	@Command(aliases = "help", help = "Displays all help information for commands in the module.")
	public Object help() {
		MetaModule meta = MetaModule.of(this);
		Module module = meta.getModule();

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("** %s**\n", module.aliases()[0]));
		builder.append(module.description() + "\n");

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
}
