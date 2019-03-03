package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.metadata.data.*;

import java.lang.reflect.Method;
import java.util.*;

public class OverloadBuilder {

    private Method method;
    private List<String> names;
    private List<ParamBuilder> params;

    public OverloadBuilder(Method method) {
        this.method = method;
        params = new ArrayList<>();
    }

    public OverloadBuilder addParam(ParamBuilder param) {
        params.add(param);
        return this;
    }

    public OverloadData build(CommandData command) {
        return new OverloadData(command, this);
    }

    public Method getMethod() {
        return method;
    }

    public List<String> getNames() {
        return names;
    }

    public OverloadBuilder setNames(List<String> names) {
        this.names = names;
        return this;
    }

    public List<ParamBuilder> getParams() {
        return params;
    }
}
