package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.Permissions;
import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.ICommandValidator;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class PermissionValidator implements ICommandValidator<Permissions> {

    @Override
    public boolean validate(CommandEvent event, Permissions annotation) {
        GenericMessageEvent e = event.getMessageEvent();
        TextChannel channel = e.getTextChannel();
        Member member = event.getMessage().getMember();
        Member self = e.getGuild().getSelfMember();
        Permission[] permissions = annotation.value();

        if (annotation.userRequiresPermission()) {
            if (!member.hasPermission(channel, permissions)) {
                String list = buildList(permissions);
                String message = "You must have the permissions: " + list + " to perform this commands.";
                return event.invalidate(message);
            }
        }

        if (!self.hasPermission(channel, permissions)) {
            String list = buildList(permissions);
            String message = "I need the permissions: " + list + " to be able to do what you just asked. :c";
            return event.invalidate(message);
        }

        return true;
    }

    @Override
    public String help(Permissions annotation) {
        return "Both the user and bot require the following permissions to do this commands: " + buildList(annotation.value()) + ".";
    }

    private String buildList(Permission[] permissions) {
        if (permissions.length == 1)
            return "`" + permissions[0].getName() + "`";

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < permissions.length; i++) {
            Permission permission = permissions[i];

            if (i != permissions.length - 1)
                builder.append("`" + permission.getName() + "`, ");
            else
                builder.append("and `" + permission.getName() + "`");
        }

        return builder.toString();
    }
}
