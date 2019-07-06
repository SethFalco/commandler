package com.elypia.commandler.doc;

import com.elypia.commandler.metadata.MetaModule;

public class CommandlerData {

    private Metadata metadata;
    private MetaModule[] modules;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public MetaModule[] getModules() {
        return modules;
    }

    public void setModules(MetaModule[] modules) {
        this.modules = modules;
    }
}
