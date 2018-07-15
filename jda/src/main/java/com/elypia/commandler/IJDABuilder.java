package com.elypia.commandler;

import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.IBuilder;

public interface IJDABuilder<I, O> implements IBuilder<I> {

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
     * {@link #buildAsString(CommandEvent, Object)} will be used as a fallback.
     *
     * @param event The Commandler managed event that trigger this.
     * @param toSend The object we're hoping to build.
     * @return The {@link MessageEmbed} ready to send to Discord.
     */

    O buildAsEmbed(CommandEvent event, I toSend);

    /**
     * Build the object into a {@link String}.
     * This is called automatically if {@link #buildAsEmbed(CommandEvent, Object)}
     * fails for whatever reason or returns null.
     *
     * @param event The Commandler managed event that trigger this.
     * @param toSend The object we're hoping to build.
     * @return The {@link String} ready to send to Discord.
     */

    O buildAsString(CommandEvent event, I toSend);
}
