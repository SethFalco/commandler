package com.elypia.commandler;

import com.elypia.commandler.metadata.*;

import java.util.*;

public class CommandInput<C, E, M> {

    private Commandler<C, E, M> commandler;

    private String content;

    private ModuleData moduleData;

    private String module;

    private CommandData commandData;

    private String command;

    private List<List<String>> parameters;

    public CommandInput(Commandler<C, E, M> commandler, String content, String module, String command, List<List<String>> parameters) {
        this.commandler = Objects.requireNonNull(commandler);
        this.content = Objects.requireNonNull(content);
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ModuleData getModuleData() {
        return moduleData;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public CommandData getCommandData() {
        return commandData;
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

    public int getParameterCount() {
        return parameters.size();
    }
}
