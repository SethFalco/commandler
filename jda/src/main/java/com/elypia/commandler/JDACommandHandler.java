package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.IgnoreGlobal;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.modules.CommandHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.*;

/**
 * The {@link JDACommandHandler} is pretty much the same as
 * the provided {@link CommandHandler} except it overrides the
 * help command to utilise {@link MessageEmbed}s if we have
 * permission to do so, else it will fall back to using the
 * default implementation.
 */

public class JDACommandHandler extends CommandHandler {

    @IgnoreGlobal
    @Command(name = "Help", aliases = "help")
    public Object help(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();

        Module annotation = module.getModule();
        builder.setTitle(annotation.name(), confiler.getHelpUrl(event));

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

                String value = "**Aliases: **" + aliasJoiner.toString() + "\n" + confiler.getHelp(event, command.help());
                List<MetaParam> metaParams = metaCommand.getInputParams();

                if (!metaParams.isEmpty()) {
                    StringJoiner helpJoiner = new StringJoiner("\n");
                    moduleHasParams = true;
                    value += "\n**Parameters:**\n";

                    metaParams.forEach(metaParam -> {
                        if (metaParam.isInput()) {
                            Param param = metaParam.getParamAnnotation();
                            helpJoiner.add("`" + param.name() + "`: " + confiler.getHelp(event, param.help()));
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
}
