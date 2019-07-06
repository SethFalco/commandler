package com.elypia.commandler.configuration;

import java.util.*;

public class Configuration implements Iterable<ModuleConfig> {

    private List<ModuleConfig> modules;

    public List<ModuleConfig> getModules() {
        return modules;
    }

    public void setModules(List<ModuleConfig> modules) {
        this.modules = modules;
    }

    @Override
    public Iterator<ModuleConfig> iterator() {
        return modules.iterator();
    }
}
