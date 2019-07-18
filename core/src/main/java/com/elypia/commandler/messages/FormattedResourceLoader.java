package com.elypia.commandler.messages;

import java.io.*;
import java.util.PropertyResourceBundle;

public class FormattedResourceLoader extends PropertyResourceBundle {

    public FormattedResourceLoader(InputStream stream) throws IOException {
        super(stream);
    }

    public FormattedResourceLoader(Reader reader) throws IOException {
        super(reader);
    }

    @Override
    public Object handleGetObject(String key) {
        Object object = super.handleGetObject(key);
        return object;
    }
}
