package com.elypia.commandler.doc;

import com.electronwill.nightconfig.core.conversion.Path;

import java.util.List;

public class DocData {

    @Path("validators")
    private List<ValidationDoc<?>> validators;
}
