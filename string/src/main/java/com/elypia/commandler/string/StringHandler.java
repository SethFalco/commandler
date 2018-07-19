package com.elypia.commandler.string;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.Ignore;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.string.client.*;

import java.util.*;

public class StringHandler extends Handler<StringClient, StringEvent, String> {

    @Ignore
    @Command(name = "Help", aliases = "help")
    public Object help(CommandEvent<StringClient, StringEvent, String> event) {
//        StringBuilder builder = new StringBuilder();
//
//        Module annotation = module.getModule();
//        builder.append(annotation.name());
//
//        if (confiler.getHelpUrl(commandler, event.getSourceEvent()) != null)
//            builder.append("\n").append("You can get more information here: " + )
//
//        if (!enabled)
//            builder.setDescription(annotation.description() + "\n**```diff\n- Disabled due to live issues! -\n```\n**");
//        else
//            builder.setDescription(annotation.description() + "\n_ _");
//
//        Iterator<MetaCommand> metaCommandIt = module.getPublicCommands().iterator();
//        boolean moduleHasParams = false;
//
//        while (metaCommandIt.hasNext()) {
//            MetaCommand metaCommand = metaCommandIt.next();
//
//            if (metaCommand.isPublic()) {
//                Command command = metaCommand.getCommand();
//                StringJoiner aliasJoiner = new StringJoiner(", ");
//
//                for (String string : command.aliases())
//                    aliasJoiner.add("`" + string + "`");
//
//                String value = "**Aliases: **" + aliasJoiner.toString() + "\n" + confiler.getHelp(event, command.help());
//                List<MetaParam> metaParams = metaCommand.getInputParams();
//
//                if (!metaParams.isEmpty()) {
//                    StringJoiner helpJoiner = new StringJoiner("\n");
//                    moduleHasParams = true;
//                    value += "\n**Parameters:**\n";
//
//                    metaParams.forEach(metaParam -> {
//                        if (metaParam.isInput()) {
//                            Param param = metaParam.getParamAnnotation();
//                            helpJoiner.add("`" + param.name() + "`: " + confiler.getHelp(event, param.help()));
//                        }
//                    });
//
//                    value += helpJoiner.toString();
//                }
//
//                if (metaCommandIt.hasNext())
//                    value += "\n_ _";
//
//                builder.addField(command.name(), value, false);
//            }
//        }
//
//        String params = moduleHasParams ? " {params}" : "";
//        String prefix = confiler.getPrefix(event.getMessageEvent());
//        String format = "Try \"%s%s {command} %s\" to perform commands!";
//        builder.setFooter(String.format(format, prefix, annotation.aliases()[0], params), null);
//
//        return builder;
        return "helped";
    }
}
