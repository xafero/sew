package com.xafero.sew.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import com.xafero.sew.api.Weblet;

public class Markdown implements Weblet, Closeable {

	private final List<Extension> extensions;
	private final Parser parser;
	private final HtmlRenderer renderer;
	private final Weblet base;

	public Markdown(Weblet base) {
		extensions = Arrays.asList(AutolinkExtension.create());
		parser = Parser.builder().extensions(extensions).build();
		renderer = HtmlRenderer.builder().extensions(extensions).build();
		this.base = base;
	}

	@Override
	public String getTextContent(String uri, String query) throws IOException {
		String raw = base.getTextContent(uri, query);
		String body = render(raw);
		String html = String.format("<html><body>%s</body></html>\n", body);
		return html;
	}

	private String render(String text) {
		Node document = parser.parse(text);
		return renderer.render(document);
	}

	@Override
	public void close() throws IOException {
		extensions.clear();
		base.close();
	}
}