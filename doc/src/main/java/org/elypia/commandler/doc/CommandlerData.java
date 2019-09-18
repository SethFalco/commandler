package org.elypia.commandler.doc;

import org.elypia.commandler.metadata.MetaController;

public class CommandlerData {

    private Metadata metadata;
    private MetaController[] modules;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public MetaController[] getModules() {
        return modules;
    }

    public void setModules(MetaController[] modules) {
        this.modules = modules;
    }
}
