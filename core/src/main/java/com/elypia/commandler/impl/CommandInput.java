package com.elypia.commandler.impl;

import com.elypia.commandler.metadata.*;

import java.util.*;

public class CommandInput<C, E, M> {

    private String module;
    private String command;
    private List<List<String>> parameters;

    private ModuleData moduleData;
    private CommandData commandData;

    public CommandInput(String module, String command, List<List<String>> parameters) {
        this.module = Objects.requireNonNull(module);
        this.command = command;
        this.parameters = Objects.requireNonNull(parameters);
    }

    @Override
    public String toString() {
        if (parameters.isEmpty())
            return "(0) None";

        StringJoiner parameterJoiner = new StringJoiner(", ");

        for (List<String> list : parameters) {
            StringJoiner itemJoiner = new StringJoiner(", ");

            for (String string : list)
                itemJoiner.add("'" + string + "'");

            if (list.size() > 1)
                parameterJoiner.add("[" + itemJoiner.toString() + "]");
            else
                parameterJoiner.add(itemJoiner.toString());
        }

        return "(" + parameters.size() + ") " + parameterJoiner.toString();
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<List<String>> parameters) {
        this.parameters = parameters;
    }

    public ModuleData getModuleData() {
        return moduleData;
    }

    public void setModuleData(ModuleData moduleData) {
        this.moduleData = moduleData;
    }

    public CommandData getCommandData() {
        return commandData;
    }

    public void setCommandData(CommandData commandData) {
        this.commandData = commandData;
    }

    public int getParameterCount() {
        return parameters.size();
    }
}
