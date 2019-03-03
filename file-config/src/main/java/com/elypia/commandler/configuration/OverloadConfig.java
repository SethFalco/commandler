package com.elypia.commandler.configuration;

import java.util.List;

public class OverloadConfig {

    private List<String> params;
    private List<ParamConfig> paramConfigs;

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public List<ParamConfig> getParamConfigs() {
        return paramConfigs;
    }

    public void setParamConfigs(List<ParamConfig> paramConfigs) {
        this.paramConfigs = paramConfigs;
    }
}
