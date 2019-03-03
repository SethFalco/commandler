package com.elypia.commandler.metadata.data;

import com.elypia.commandler.interfaces.Parser;
import com.elypia.commandler.metadata.builder.ParserBuilder;

import java.util.List;

public class ParserData {

    private Class<? extends Parser> clazz;
    private List<Class<?>> compatible;

    public ParserData(ParserBuilder builder) {
        this.clazz = builder.getParserClass();
        this.compatible = builder.getCompatible();
    }

    public Class<? extends Parser> getParserClass() {
        return clazz;
    }

    public List<Class<?>> getCompatible() {
        return compatible;
    }
}
