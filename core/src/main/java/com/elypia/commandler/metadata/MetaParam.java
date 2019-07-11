package com.elypia.commandler.metadata;

import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

public class MetaParam {

    private Class<?> type;

    /** The method or parameter that represents this param. */
    private AnnotatedElement annotatedElement;

    /** The name of this  */
    private String name;

    /** Any helper description to let users know what this is. */
    private String help;

    /** The default value if any else null. */
    private String defaultValue;

    /** If this parameter is required when executing the command. */
    private boolean isOptional;

    /** If this is a single parameter, or a list parameter. */
    private boolean isList;

    public MetaParam(Class<?> type, AnnotatedElement annotatedElement, String name, String help, String defaultValue) {
        this.type = Objects.requireNonNull(type);
        this.annotatedElement = Objects.requireNonNull(annotatedElement);
        this.name = Objects.requireNonNull(name);
        this.help = help;
        this.defaultValue = defaultValue;

        isOptional = defaultValue != null;
        isList = type.isArray();
    }

    public Class<?> getType() {
        return type;
    }

    public AnnotatedElement getAnnotatedElement() {
        return annotatedElement;
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public boolean isRequired() {
        return !isOptional;
    }

    public boolean isList() {
        return isList;
    }
}
