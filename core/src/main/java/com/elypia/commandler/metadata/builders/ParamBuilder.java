package com.elypia.commandler.metadata.builders;

import com.elypia.commandler.metadata.MetaParam;

import java.lang.reflect.*;
import java.util.Objects;

public class ParamBuilder {

    private Class<?> type;
    private AnnotatedElement annotatedElement;
    private String name;
    private String help;
    private String[] defaultValue;

    public ParamBuilder(Class<?> type, AnnotatedElement annotatedElement) {
        this.type = Objects.requireNonNull(type);
        this.annotatedElement = Objects.requireNonNull(annotatedElement);
    }

    public MetaParam build(CommandBuilder builder) {
        return new MetaParam(type, annotatedElement, name, help, defaultValue);
    }

    public Class<?> getType() {
        return type;
    }

    public AnnotatedElement getAnnotatedElement() {
        return annotatedElement;
    }

    public ParamBuilder setAnnotatedElement(AccessibleObject object) {
        this.annotatedElement = object;
        return this;
    }

    public String getName() {
        return name;
    }

    public ParamBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getHelp() {
        return help;
    }

    public ParamBuilder setHelp(String help) {
        this.help = help;
        return this;
    }

    public String[] getDefaultValue() {
        return defaultValue;
    }

    public ParamBuilder setDefaultValue(String[] defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
}
