package com.elypia.commandler.jda.validation.command;

import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Permissions;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class PermissionValidator implements IJDACommandValidator<Permissions> {

    @Override
    public boolean validate(JDACommand event, Permissions annotation) {
        GenericMessageEvent e = event.getSource();
        TextChannel channel = e.getTextChannel();
        Member member = event.getMessage().getMember();
        Member self = e.getGuild().getSelfMember();
        Permission[] permissions = annotation.value();

        if (annotation.userRequiresPermission()) {
            if (!member.hasPermission(channel, permissions))
                return false;
        }

        if (!self.hasPermission(channel, permissions))
            return false;

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
