package com.elypia.commandler;

import com.elypia.commandler.components.Parsers;
import com.elypia.commandler.impl.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

/**
 * This is an extention to the normal {@link IBuilder}
 * which defaults the parameterised types where possible
 * and forced the user to define a second method for each builder
 * so we can build is as an embed, or as a normal message.
 *
 * @param <O> The object to build, this would have been the
 *            output from the {@link Parsers}.
 */
public interface IJDABuilder<O> extends IBuilder<O, Message>
{

    /**
     * Build the object into an {@link MessageEmbed}. <br>
     * Embeds are much more attractive than standard {@link String}
     * {@link Message messages} however it's possible for guilds
     * to revoke the {@link Permission permission} to send
     * them via the {@link Permission#MESSAGE_EMBED_LINKS} {@link Permission permission}.
     * They can also be disabled on the users side with no way to remotely identify
     * this.
     *
     * <ul>
     *     <li>When a guild revokes the permission for a bot to perform send embeds, they simply fail to send.</li>
     *     <li>Users that have embeds disabled locally just see an empty message from the bot if nothing else is preventing it from sending.</li>
     * </ul>
     *
     * {@link Commandler} will attempt to build the object as an {@link MessageEmbed}
     * however if null is returned, an error occurs, or the bot lacks permission,
     * {@link #build(ICommandEvent, Object)} will be used as a fallback,
     * which should return a {@link String}.
     *
     * @param event The Commandler managed event that trigger this.
     * @param output The object we're hoping to build.
     * @return The {@link MessageEmbed} ready to send to Discord.
     */
    Message buildAsEmbed(CommandEvent<JDA, GenericMessageEvent, Message> event, O output);
}
