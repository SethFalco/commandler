package com.elypia.commandler;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.metadata.MetaModule;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;

import java.util.StringJoiner;

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
	public Object help() {
		MetaModule meta = MetaModule.of(this);
		Module module = meta.getModule();

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(module.name());

		StringJoiner joiner = new StringJoiner(", ");

		for (String string : module.aliases())
			joiner.add("`" + string + "`");

		String description = "**Aliases**: " + joiner.toString() + "\n" + module.description();
		builder.setDescription(description);

		meta.getCommands().forEach(metaCommand -> {
			Command command = metaCommand.getCommand();

			if (!command.help().isEmpty())
				builder.addField(command.aliases()[0], command.help(), false);
		});

		return builder;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public JDA getJDA() {
		return jda;
	}

	public void setJDA(JDA jda) {
		this.jda = jda;
	}
}
