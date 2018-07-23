package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.Ignore;
import com.elypia.commandler.metadata.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;

/**
 * The {@link JDAHandler} is pretty much the same as
 * the provided {@link Handler} except it overrides the
 * help command to utilise {@link MessageEmbed}s if we have
 * permission to do so, else it will fall back to using the
 * default implementation.
 */
public class JDAHandler extends Handler<JDA, GenericMessageEvent, Message> {

    @Ignore
    @Command(name = "Help", aliases = "help")
    public Object help(JDACommand event) {
        EmbedBuilder builder = new EmbedBuilder();

        Module annotation = module.getModule();
        builder.setTitle(annotation.name(), confiler.getHelpUrl(commandler, event.getSource()));

        if (!enabled)
            builder.setDescription(annotation.help() + "\n**```diff\n- Disabled due to live issues! -\n```\n**");
        else
            builder.setDescription(annotation.help() + "\n_ _");

        Iterator<MetaCommand<JDA, GenericMessageEvent, Message>> metaCommandIt = module.getPublicCommands().iterator();
        boolean moduleHasParams = false;

        while (metaCommandIt.hasNext()) {
            MetaCommand metaCommand = metaCommandIt.next();

            if (metaCommand.isPublic()) {
                Command command = metaCommand.getCommand();
                StringJoiner aliasJoiner = new StringJoiner(", ");

                for (String string : command.aliases())
                    aliasJoiner.add("`" + string + "`");

                String value = "**Aliases: **" + aliasJoiner.toString() + "\n" + confiler.getHelp(commandler, event.getSource(), command.help());
                List<MetaParam> metaParams = metaCommand.getInputParams();

                if (!metaParams.isEmpty()) {
                    StringJoiner helpJoiner = new StringJoiner("\n");
                    moduleHasParams = true;
                    value += "\n**Parameters:**\n";

                    metaParams.forEach(metaParam -> {
                        if (metaParam.isInput()) {
                            Param param = metaParam.getParamAnnotation();
                            helpJoiner.add("`" + param.name() + "`: " + confiler.getHelp(commandler, event.getSource(), param.help()));
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
        String prefix = confiler.getPrefixes(commandler, event.getSource())[0];
        String format = "Try \"%s%s {command} %s\" to perform commands!";
        builder.setFooter(String.format(format, prefix, annotation.aliases()[0], params), null);

        return builder;
    }
}
