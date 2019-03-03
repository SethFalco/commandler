package com.elypia.commandler.configuration;

import com.electronwill.nightconfig.core.conversion.Path;

import java.lang.reflect.Method;
import java.util.*;

public class CommandConfig implements Iterable<ParamConfig> {

    private Method method;
    private String name;
    private List<String> aliases;
    private String help;

    @Path("hidden")
    private boolean isHidden;

    @Path("static")
    private boolean isStatic;

    @Path("default")
    private boolean isDefault;

    private List<ParamConfig> params;

    private List<OverloadConfig> overloads;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public List<ParamConfig> getParams() {
        return params;
    }

    public void setParams(List<ParamConfig> params) {
        this.params = params;
    }

    public List<OverloadConfig> getOverloads() {
        return overloads;
    }

    public void setOverloads(List<OverloadConfig> overloads) {
        this.overloads = overloads;
    }

    @Override
    public Iterator<ParamConfig> iterator() {
        return params.iterator();
    }
}
