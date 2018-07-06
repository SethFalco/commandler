package com.elypia.commandler.metadata;

import com.elypia.commandler.annotations.Overload;

import java.lang.reflect.Method;
import java.util.*;

public class MetaOverload extends AbstractMetaCommand implements Comparable<MetaOverload> {

    private MetaCommand command;

    private Overload overload;

    public MetaOverload(MetaCommand metaCommand, Method method, Overload overload) {
        super(metaCommand.getMetaModule(), method);
        this.overload = Objects.requireNonNull(overload);
    }

    @Override
    protected void parseParams() {
        String[] params = overload.params();

        if (params.length == 1 && params[0].equals(Overload.INHERIT)) {
            metaParams.addAll(command.getMetaParams());

            return;
        }



        for (String param : params) {

        }
    }

    @Override
    protected void parseAnnotations() {

    }

    @Override
    public int compareTo(MetaOverload overload) {
        return getInputRequired() > overload.getInputRequired() ? -1 : 1;
    }
}
