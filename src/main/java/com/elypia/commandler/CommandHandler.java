package com.elypia.commandler;

import com.elypia.commandler.annotations.command.Command;
import com.elypia.commandler.annotations.command.Module;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaModule;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

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
		MetaModule module = new MetaModule(this);

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("** %s**\n", module.aliases()[0]));
		builder.append(module.help() + "\n");

		for (Command command : commands) {
			if (!command.help().isEmpty()) {
				builder.append(String.format("`%s`: %s\n", command.aliases()[0], command.help()));
			}
		}

		return "";
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
