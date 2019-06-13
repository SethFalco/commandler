package com.elypia.commandler.metadata.data;

import com.elypia.commandler.metadata.builder.OverloadBuilder;

import java.lang.reflect.Method;
import java.util.*;

public class OverloadData {

    private Method method;
    private List<ParamData> params;

    public OverloadData(MetaCommand command, OverloadBuilder builder) {
        this.method = builder.getMethod();
        params = new ArrayList<>();

        for (String name : builder.getNames())
            params.add(command.getParams().stream().filter(o -> o.getName().equals(name)).findAny().get());
    }

    public String toParamsString() {
        if (params == null)
            return "Uninitialized";

        if (params.isEmpty())
            return "(0) None";

        StringJoiner itemJoiner = new StringJoiner(", ");

        for (ParamData param : params) {
            String name = param.getName();

            if (param.isList())
                itemJoiner.add("['" + name + "']");
            else
                itemJoiner.add("'" + name + "'");
        }

        return "(" + params.size() + ") " + itemJoiner.toString();
    }

    public Method getMethod() {
        return method;
    }

    public List<ParamData> getParams() {
        return params;
    }
}
