package com.elypia.commandler.configuration;

import com.electronwill.nightconfig.core.conversion.Path;
import com.elypia.commandler.Handler;

import java.util.*;

public class ModuleConfig implements Iterable<CommandConfig> {

    /**
     * The class this configuration represents.
     */
    private Class<? extends Handler> handler;

    /**
     * The name of this module. <br>
     * If null, defaults to the the naming convention: <strong>{name}Module</strong> <br>
     * Example: <br>
     * BotModule => Bot <br>
     * MyModule => My Module
     */
    private String name;

    private String group;

    private List<String> aliases;

    private String help;

    @Path("hidden")
    private boolean isHidden;

    private List<CommandConfig> commands;

    public Class<? extends Handler> getHandler() {
        return handler;
    }

    public void setHandler(Class<? extends Handler> handler) {
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public List<CommandConfig> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandConfig> commands) {
        this.commands = commands;
    }

    @Override
    public Iterator<CommandConfig> iterator() {
        return commands.iterator();
    }
}
