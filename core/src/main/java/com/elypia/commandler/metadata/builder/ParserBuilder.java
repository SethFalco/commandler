package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.interfaces.Parser;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.data.ParserData;

import java.util.List;

public class ParserBuilder {

    private Class<? extends Parser> clazz;
    private List<Class<?>> compatible;

    public ParserBuilder(Class<? extends Parser> clazz) {
        this.clazz = clazz;
    }

    public ParserData build(ContextLoader loader) {
        return new ParserData(this);
    }

    public Class<? extends Parser> getParserClass() {
        return clazz;
    }

    public List<Class<?>> getCompatible() {
        return compatible;
    }

    public ParserBuilder setCompatible(List<Class<?>> compatible) {
        this.compatible = compatible;
        return this;
    }
}
