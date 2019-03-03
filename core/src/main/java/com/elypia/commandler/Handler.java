package com.elypia.commandler;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.*;
import com.google.inject.Inject;

import java.util.*;

public abstract class Handler<S, M> {

	@Inject
	protected Commandler commandler;

	@Inject
	protected LanguageAdapter language;

	/**
	 * Performs relevent tests to ensure this module
	 * is still working as intended. Should any test fail we will
	 * still load and display {@link #help(CommandlerEvent)} for the module
	 * however all other commands will not be possible.
	 *
	 * @return If the module should remain enabled.
	 */
	public boolean test() {
		return true;
	}

	/**
	 * The default value command for a {@link Handler},
	 * this should use the {@link ModuleData} around
	 * this {@link Handler} to display helpful information
	 * to the user.
	 *
	 * @param event The {@link CommandlerEvent event} produced by Commandler.
	 * @return The message to send to the end user.
	 */
	@Command(name = "Help", aliases = "help")
	public Object help(CommandlerEvent<S, M> event) {
        StringBuilder builder = new StringBuilder(getModule().getName());

		builder
            .append(" (")
            .append(String.join(", ", getModule().getAliases()))
            .append(")\n")
            .append(getModule().getHelp());

		if (commandler.getTestRunner().isFailing(this))
			builder.append("\n").append(getCommandler().getMisuseHandler().onModuleDisabled(event));

		builder.append("\n\n");

		Iterator<CommandData> metaCommandIt = getModule().getPublicCommands().iterator();

		while (metaCommandIt.hasNext()) {
			CommandData command = metaCommandIt.next();
			builder.append(language.get(event.getSource(), command.getName()));

			builder
                .append(" (")
                .append(String.join(", ", command.getAliases()))
                .append(")\n")
			    .append(language.get(event.getSource(), command.getHelp()));

			List<ParamData> params = command.getParams();

			params.forEach((param) -> {
				builder.append("\n" + language.get(event.getSource(), param.getName()) + ": ");
				builder.append(language.get(event.getSource(), param.getHelp()));
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
		return commandler.getContext().getModule(this.getClass());
	}
}
