package com.elypia.commandler;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.*;

import java.util.*;

public abstract class Handler<S, M> {

	protected Commandler<S, M> commandler;

	protected ModuleData module;

	protected IScripts<S> scripts;

	/**
	 * If this module is enabled or out of service.
	 */
	protected boolean enabled;

	/**
	 * Initialise the module, this will assign the values
	 * in the module and create a {@link ModuleData} which is
	 * what {@link Commandler} uses in runtime to identify modules,
	 * commands or obtain any static data.
	 *
	 * @param commandler Our parent Commandler class.
	 * @return Returns if the {@link #test()} for this module passed.
	 */
	public boolean init(Commandler<S, M> commandler) {
		this.commandler = commandler;
		module = commandler.getContext().getModule(this.getClass());
		scripts = commandler.getEngine();
		enabled = test();
		return enabled;
	}

	/**
	 * Performs relevent tests to ensure this module
	 * is still working as intended. Should any test fail we will
	 * still load and display {@link #help(ICommandEvent)} for the module
	 * however all other commands will not be possible.
	 *
	 * @return If the module should remain enabled.
	 */
	public boolean test() {
		return true;
	}

	/**
	 * The default help command for a {@link Handler},
	 * this should use the {@link ModuleData} around
	 * this {@link Handler} to display helpful information
	 * to the user.
	 *
	 * @param event The {@link ICommandEvent event} produced by Commandler.
	 * @return The message to send to the end user.
	 */
	@Command(id = "Help", aliases = "help")
	public Object help(ICommandEvent<S, M> event) {
		StringBuilder builder = new StringBuilder();

		Module annotation = getModule().getAnnotation();
		builder.append(annotation.id());

		StringJoiner commandAliasJoiner = new StringJoiner(", ");

		for (String alias : annotation.aliases())
			commandAliasJoiner.add(alias);

		builder.append(" (" + commandAliasJoiner.toString() + ")");
		builder.append("\n" + annotation.help());

		if (!isEnabled())
			builder.append("\n" + getCommandler().getMisuseHandler().onModuleDisabled(event));

		builder.append("\n\n");

		Iterator<CommandData> metaCommandIt = getModule().getPublicCommands().iterator();

		while (metaCommandIt.hasNext()) {
			CommandData commandData = metaCommandIt.next();
			Command command = commandData.getAnnotation();
			builder.append(command.id());

			StringJoiner aliasJoiner = new StringJoiner(", ");

			for (String string : command.aliases())
				aliasJoiner.add(string);

			builder.append(" (" + aliasJoiner.toString() + ")");
			builder.append("\n" + command.help());

			List<ParamData> paramData = commandData.getInputParams();

			paramData.forEach(metaParam -> {
				Param param = metaParam.getAnnotation();
				builder.append("\n" + param.id() + ": ");
				builder.append(scripts.get(event.getSource(), param.help()));
			});

			if (metaCommandIt.hasNext())
				builder.append("\n\n");
		}

		String helpUrl = commandler.getWebsite();

		if (helpUrl != null)
			builder.append(helpUrl);

		return builder.toString();
	}

	public Commandler<S, M> getCommandler() {
		return commandler;
	}

	public ModuleData getModule() {
		return module;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int compareTo(Handler<S, M> o) {
		return module.compareTo(o.getModule());
	}
}
