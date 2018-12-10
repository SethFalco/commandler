package com.elypia.commandler.doc;

import org.commonmark.Extension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.*;
import java.util.List;

public class MarkdownParser {

    private Parser parser;
    private HtmlRenderer renderer;

    public MarkdownParser(Extension... extensions) {
        List<Extension> list = List.of(extensions);

        parser = Parser.builder().extensions(list).build();
        renderer = HtmlRenderer.builder().extensions(list).build();
    }

    public String parse(String body) {
        Node node = parser.parse(body);
        return renderer.render(node);
    }

    public String parseFile(String path) throws IOException {
        Node node = parser.parseReader(new FileReader(path));
        return renderer.render(node);
    }
}
