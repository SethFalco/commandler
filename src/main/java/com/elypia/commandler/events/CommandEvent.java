package com.elypia.commandler.events;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.metadata.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.*;

import java.util.*;
import java.util.regex.*;

public class CommandEvent extends AbstractEvent {

    /**
     * If the commands performed is considered a valid commands at all.
     */

	private boolean isValid;

    /**
     * The root alias refers to the first segment of a commands which may be the alias of a {@link Module} <br>
     * <strong>Possible null</strong>: If commands is not {@link #isValid() valid}.
     */

	private String module;

	/**
	 * The module associated with this command if the module is valid.
	 */

	private MetaModule metaModule;

    /**
     * The commands segment of this commands if valid, this refers to which commands
     * in the specified module to perform. <br>
     * <br>
     * <strong>Possible null</strong>: <br>
     * If commands is not {@link #isValid() valid}.
     */

	private String command;

    /**
     * Calls {@link #CommandEvent(Commandler, GenericMessageEvent, Message, String)}
     * with the content as this the content of this message.
     *
     * @param commandler The parent {@link Commandler} instance spawning this event.
     * @param event The {@link MessageReceivedEvent} or {@link MessageUpdateEvent} as provided by {@link JDA}.
     * @param message The {@link Message} to attempt to process as a commands.
     */

	public CommandEvent(Commandler commandler, GenericMessageEvent event, Message message) {
		this(commandler, event, message, message.getContentRaw());
	}

    /**
     *  Parse the {@link JDA} {@link GenericMessageEvent} into an event object {@link Commandler}
     *  will better utilise.
     *
     * @param commandler The parent {@link Commandler} instance spawning this event.
     * @param event The {@link MessageReceivedEvent} or {@link MessageUpdateEvent} as provided by {@link JDA}.
     * @param message The {@link Message} to attempt to process as a commands.
     * @param content The commands itself, this may be different from the contents of the message sent.
     */

	public CommandEvent(Commandler commandler, GenericMessageEvent event, Message message, String content) {
		super(commandler, event);
		this.message = message;

		confiler = commandler.getConfiler();
		params = new ArrayList<>();

		Pattern pattern = confiler.getCommandRegex(event);
		Matcher matcher = pattern.matcher(content);

		isValid = matcher.matches();

		if (!isValid)
			return;

		module = matcher.group("module");
		command = matcher.group("commands");

		String parameters = matcher.group("params");

		if (parameters != null) {
			Pattern splitPattern = confiler.getSplitRegex(event);
			matcher = confiler.getParamRegex(event).matcher(parameters);

			while (matcher.find()) {
				String group = matcher.group();
				Matcher splitMatcher = splitPattern.matcher(group);

				List<String> input = new ArrayList<>();
				while (splitMatcher.find()) {
					String quote = splitMatcher.group("quote");
					input.add(quote != null ?  quote : splitMatcher.group("word"));
				}

				params.add(input);
			}
		}
	}

	/**
	 * If an error occurs during the command, if for whatever reason
	 * the syntax is valid but the command event can't be performed,
	 * invalidate the command with the reason why we can't perform it.
	 * If the value is null, fail silently and don't send a message. <br>
	 * The reason for this is sent in the channel the command was performed in
	 * if not null.
	 *
	 * @param reason The message to post as to why we aren't going to perform the command.
	 * @return The new state of {@link #isValid}; this should <strong>always</strong> be <em>false</em>.
	 */

	public boolean invalidate(String reason) {
		isValid = false;

		if (reason != null)
			event.getChannel().sendMessage(reason).queue();

		return isValid;
	}

    /**
     * @return If message sent actually fits the commands format as
     * dictated in {@link Confiler#getCommandRegex(GenericMessageEvent)}.
     */

	public boolean isValid() {
		return isValid;
	}

    /**
     * @return Get the module of a command, this is normally the first thing
     * after the prefix.
     */

	public String getModule() {
		return module;
	}

	public MetaModule getMetaModule() {
		return metaModule;
	}

	public void setMetaModule(MetaModule metaModule) {
		this.metaModule = metaModule;
		module = metaModule.getModule().aliases()[0];
	}

    /**
     * @return Get the commands for this event.
     */

	public String getCommand() {
		return command;
	}

	public void setMetaCommand(AbstractMetaCommand metaCommand) {
		this.metaCommand = metaCommand;
		command = metaCommand.getCommand().aliases()[0];
	}
}
