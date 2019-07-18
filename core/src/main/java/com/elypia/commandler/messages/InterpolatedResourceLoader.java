package com.elypia.commandler.messages;

import java.io.*;

// TODO: Allow text to be noted as non-translated
// TODO: Allow "options" which tell this to use the options
// TODO: Allow "repeat" which tells this to use x to y of it.
// TODO: Formatted
public class InterpolatedResourceLoader extends ReflectiveResourceLoader {

    public InterpolatedResourceLoader(InputStream stream) throws IOException {
        super(stream);
    }

    public InterpolatedResourceLoader(Reader reader) throws IOException {
        super(reader);
    }

    @Override
    public Object handleGetObject(String key) {
        Object object = super.handleGetObject(key);
        return object;
    }
}
