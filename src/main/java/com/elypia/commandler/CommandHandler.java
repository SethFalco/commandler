package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.MetaModule;
import net.dv8tion.jda.core.*;

import java.util.StringJoiner;

public abstract class CommandHandler {

	protected JDA jda;

	protected MetaModule module;

	/**
	 * If this module is enabled or out of service.
	 */

	protected boolean enabled;

	public CommandHandler() {
		module = MetaModule.of(this);
	}

	public boolean test() {
		return true;
	}

	@Command(aliases = "help", help = "Displays all help information for commands in the module.")
	public Object help() {
		Module annotation = module.getModule();

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(annotation.name());

		StringJoiner joiner = new StringJoiner(", ");

		for (String string : annotation.aliases())
			joiner.add("`" + string + "`");

		String description = "**Aliases**: " + joiner.toString() + "\n" + annotation.description();
		builder.setDescription(description);

		module.getCommands().forEach(metaCommand -> {
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

	public MetaModule getModule() {
		return module;
	}
}
