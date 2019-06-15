package com.elypia.commandler.metadata.data;

import com.elypia.commandler.interfaces.DynDefaultValue;

import java.util.Objects;

public class MetaParam {

    /** The type of parameter this is. */
    private Class<?> type;

    /** The name of this  */
    private String name;

    /** Any helper description to let users know what this is. */
    private String help;

    /** The default value if any else null. */
    private String defaultValue;

    /** The default value via method if any else null. */
    private Class<? extends DynDefaultValue> dynDefaultValue;

    /** If this parameter is required when executing the command. */
    private boolean isOptional;

    /** If this is a single parameter, or a list parameter. */
    private boolean isList;

    public MetaParam(Class<?> type, String name, String help, String defaultValue) {
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
        this.help = help;
        this.defaultValue = defaultValue;

        isOptional = defaultValue != null && dynDefaultValue == null;
        isList = type.isArray();
    }

    public Class<?> getType() {
        return type;
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

    public Class<? extends DynDefaultValue> getDynDefaultValue() {
        return dynDefaultValue;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public boolean isList() {
        return isList;
    }
}
