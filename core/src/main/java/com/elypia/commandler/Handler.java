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
	 * Initialise the module, this will assign the values
	 * in the module and create a {@link ModuleData} which is
	 * what {@link Commandler} uses in runtime to identify modules,
	 * commands or obtain any static data.
	 *
	 * @param commandler Our parent Commandler class.
	 */
	public Handler(Commandler<S, M> commandler) {
		this.commandler = commandler;
		module = commandler.getContext().getModule(this.getClass());
		scripts = commandler.getEngine();
		commandler.getTestRunner().test(this);
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
        Module annotation = getModule().getAnnotation();

        StringBuilder builder = new StringBuilder(annotation.id());

		builder
            .append(" (")
            .append(String.join(", ", annotation.aliases()))
            .append(")\n")
            .append(annotation.help());

		if (commandler.getTestRunner().isFailing(this))
			builder.append("\n").append(getCommandler().getMisuseHandler().onModuleDisabled(event));

		builder.append("\n\n");

		Iterator<CommandData> metaCommandIt = getModule().getPublicCommands().iterator();

		while (metaCommandIt.hasNext()) {
			CommandData commandData = metaCommandIt.next();
			Command command = commandData.getAnnotation();
			builder.append(scripts.get(event.getSource(), command.id()));

			builder
                .append(" (")
                .append(String.join(", ", command.aliases()))
                .append(")\n")
			    .append(scripts.get(event.getSource(), command.help()));

			List<ParamData> paramData = commandData.getInputParams();

			paramData.forEach(metaParam -> {
				Param param = metaParam.getAnnotation();
				builder.append("\n" + scripts.get(event.getSource(), param.id()) + ": ");
				builder.append(scripts.get(event.getSource(), param.help()));
			});

			if (metaCommandIt.hasNext())
				builder.append("\n\n");
		}

		String helpUrl = commandler.getWebsite();

		if (helpUrl != null)
			builder.append("\n").append(helpUrl);

		return builder.toString();
	}

	public Commandler<S, M> getCommandler() {
		return commandler;
	}

	public ModuleData getModule() {
		return module;
	}

	public int compareTo(Handler<S, M> o) {
		return module.compareTo(o.getModule());
	}
}
