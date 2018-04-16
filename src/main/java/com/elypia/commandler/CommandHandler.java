package com.elypia.commandler;

import com.elypia.commandler.annotations.command.Command;
import com.elypia.commandler.annotations.command.Module;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaModule;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public abstract class CommandHandler {

	/**
	 * If this module is enabled or out of service.
	 */

	protected boolean enabled;

	public boolean test() {
		return true;
	}

	@Command(aliases = "help", help = "Displays all help information for commands in the module.")
	public void help(MessageEvent event) {
		MetaModule module = new MetaModule(this);

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("** %s**\n", module.aliases()[0]));
		builder.append(module.help() + "\n");

		for (Command command : commands) {
			if (!command.help().isEmpty()) {
				builder.append(String.format("`%s`: %s\n", command.aliases()[0], command.help()));
			}
		}

		event.reply(builder.toString());
	}

	public boolean isEnabled() {
		return enabled;
	}
}
