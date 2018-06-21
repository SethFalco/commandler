package com.elypia.commandler.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.*;
import net.dv8tion.jda.core.*;

import java.util.*;

public abstract class CommandHandler implements Comparable<CommandHandler> {

	protected JDA jda;

	protected Commandler commandler;
	protected Confiler confiler;
	protected MetaModule module;

	/**
	 * If this module is enabled or out of service.
	 */

	protected boolean enabled;

	public boolean init(JDA jda, Commandler commandler) {
		this.jda = jda;
		this.commandler = commandler;
		confiler = commandler.getConfiler();
		module = MetaModule.of(commandler, this);
		return test();
	}

	public boolean test() {
		return true;
	}

	@IgnoreGlobal // Don't associate any GlobalValidation with this command
	@Command(name = "Help", aliases = "help")
	public Object help(MessageEvent event) {
		EmbedBuilder builder = new EmbedBuilder();

		Module annotation = module.getModule();
		builder.setTitle(annotation.name());

		if (!enabled)
			builder.setDescription(annotation.description() + "\n**```diff\n- Disabled due to live issues! -\n```\n**");
		else
			builder.setDescription(annotation.description() + "\n_ _");

		Iterator<MetaCommand> metaCommandIt = module.getPublicCommands().iterator();
		boolean moduleHasParams = false;

		while (metaCommandIt.hasNext()) {
			MetaCommand metaCommand = metaCommandIt.next();

			if (metaCommand.isPublic()) {
				Command command = metaCommand.getCommand();
				StringJoiner aliasJoiner = new StringJoiner(", ");

				for (String string : command.aliases())
					aliasJoiner.add("`" + string + "`");

				String value = "**Aliases: **" + aliasJoiner.toString() + "\n" + command.help();
				List<MetaParam> metaParams = metaCommand.getInputParams();

				if (!metaParams.isEmpty()) {
					StringJoiner helpJoiner = new StringJoiner("\n");
					moduleHasParams = true;
					value += "\n";

					metaParams.forEach(metaParam -> {
						if (metaParam.isInput()) {
							Param param = metaParam.getParamAnnotation();
							helpJoiner.add("`" + param.name() + "`: " + param.help());
						}
					});

					value += helpJoiner.toString();
				}

				if (metaCommandIt.hasNext())
					value += "\n_ _";

				builder.addField(command.name(), value, false);
			}
		}

		String params = moduleHasParams ? " {params}" : "";
		String prefix = confiler.getPrefix(event.getMessageEvent());
		String format = "Try \"%s%s {command} %s\" to perform commands!";
		builder.setFooter(String.format(format, prefix, annotation.aliases()[0], params), null);

		return builder;
	}

	public JDA getJDA() {
		return jda;
	}

	public Commandler getCommandler() {
		return commandler;
	}

	public Confiler getConfiler() {
		return confiler;
	}

	public MetaModule getModule() {
		return module;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int compareTo(CommandHandler o) {
		return module.compareTo(o.module);
	}
}
