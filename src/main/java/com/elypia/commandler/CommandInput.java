package com.elypia.commandler;

import java.util.List;

public class CommandInput {

    private String content;

    private String module;

    private String command;

    private List<List<String>> parameters;

    public CommandInput() {

    }

    public CommandInput(String content, String module, String command, List<List<String>> parameters) {
        this.content = content;
        this.module = module;
        this.command = command;
        this.parameters = parameters;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
