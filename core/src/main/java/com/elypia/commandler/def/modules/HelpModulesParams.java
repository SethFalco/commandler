package com.elypia.commandler.def.modules;

import com.elypia.commandler.annotations.Param;

public class HelpModulesParams {

    @Param(
        name = "group",
        value = "The group to list modules for."
    )
    private String query;

    public String getQuery() {
        return query;
    }
}
