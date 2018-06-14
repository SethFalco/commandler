package com.elypia.commandler.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.*;
import net.dv8tion.jda.core.*;

import java.util.*;

public abstract class CommandHandler {

	protected JDA jda;

	protected Commandler commandler;
	protected MetaModule module;

	/**
	 * If this module is enabled or out of service.
	 */

	protected boolean enabled;

	/**
	 * Should the bot continue executing {@link #test()} or should it
	 * give up on this module for the session.
	 */

	protected boolean tryAgain;

	public CommandHandler() {
		tryAgain = true;
	}

	public boolean init(JDA jda, Commandler commandler) {
		this.jda = jda;
		this.commandler = commandler;
		module = MetaModule.of(commandler, this);
		return test();
	}

	public boolean test() {
		return true;
	}

	@Command(name = "Help", aliases = "help")
	public Object help(MessageEvent event) {
		Confiler confiler = commandler.getConfiler();
		String prefix = confiler.getPrefix(event.getMessageEvent());
		Module annotation = module.getModule();
		EmbedBuilder builder = new EmbedBuilder();

		builder.setTitle(annotation.name());
		String description = annotation.description();

		if (!enabled)
			description += "\n```diff\n- This module is currently disabled due to live issues. -```";

		builder.setDescription(description + "\n_ _");

		Iterator<MetaCommand> iter = module.getCommands().iterator();
		boolean globalParams = false;

		while (iter.hasNext()) {
			MetaCommand metaCommand = iter.next();

			Command command = metaCommand.getCommand();



			if (!command.help().isEmpty()) {
				String[] aliases = command.aliases();
				StringJoiner stringJoiner = new StringJoiner(", ");

				for (String string : aliases)
					stringJoiner.add("`" + string + "`");

				String value = "**Aliases: **" + stringJoiner.toString() + "\n" + command.help();

				StringJoiner helpJoiner = new StringJoiner("\n");

				Iterator<MetaParam> it = metaCommand.getParams().iterator();

				boolean params = false;

				while (it.hasNext()) {
					MetaParam metaParam = it.next();
					Param param = metaParam.getParams();

					if (param != null) {
						if (!params) {
							globalParams = true;
							params = true;
							value += "\n";
						}

						String text = "`" + param.name() + "`: " + param.help();
						helpJoiner.add(text);
					}
				}

				value += helpJoiner.toString();

				if (iter.hasNext())
					value += "\n_ _";

				builder.addField(command.name(), value, false);
			}
		}

		String includeGlobal = globalParams ? " {params}" : "";
		builder.setFooter("Try \"" + prefix + annotation.aliases()[0] + " {command}" + includeGlobal + "\" to perform commands!", null);

		return builder;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean shouldTryAgain() {
		return tryAgain;
	}

	public void setTryAgain(boolean tryAgain) {
		this.tryAgain = tryAgain;
	}

	public JDA getJDA() {
		return jda;
	}

	public Commandler getCommandler() {
		return commandler;
	}

	public MetaModule getModule() {
		return module;
	}
}
